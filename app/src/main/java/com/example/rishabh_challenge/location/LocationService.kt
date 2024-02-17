package com.example.rishabh_challenge.location

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rishabh_challenge.R
import com.example.rishabh_challenge.db.TrekkingRepository
import com.example.rishabh_challenge.ui.MainActivity
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationService: Service() {
    // scope for the lifetime of our service
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private var oldLocation: Location? = null
    private val trekkingRepository = TrekkingRepository(this)

    override fun onBind(p0: Intent?): IBinder? {
       return null
    }

    override fun onCreate() {
        super.onCreate()

        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Re-Imagining your Trek")
            .setContentText("Location: ")
            .setSmallIcon(R.drawable.ic_launcher)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(1000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                Log.d("LOCATION", location.toString())
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(NOTIFICATION_ID, updatedNotification.build())
                if(oldLocation != null){
                    if(haversine(location.latitude, location.longitude, oldLocation!!.latitude, oldLocation!!.longitude)){
                        trekkingRepository.getPhotoFromFlickr(location.latitude, location.longitude)
                        oldLocation = location
                    }
                }
                else{
                    oldLocation = location
                }
            }
            .launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        trekkingRepository.clearTrek()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val NOTIFICATION_ID = 777
    }


    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        val R = 6371.0 // Earth radius in kilometers

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = R * c * 1000 // Convert distance to meters
        Log.d("LOCATION", "distance between loc: $distance")
        return distance > 100
    }

}