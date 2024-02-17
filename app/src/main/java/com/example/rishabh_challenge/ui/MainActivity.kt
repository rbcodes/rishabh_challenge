package com.example.rishabh_challenge.ui

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.searchdebounce.ui.PhotosAdapter
import com.example.android.searchdebounce.ui.PhotosViewModel
import com.example.rishabh_challenge.R
import com.example.rishabh_challenge.databinding.ActivityMainBinding
import com.example.rishabh_challenge.db.TrekkingRepository
import com.example.rishabh_challenge.extensions.hasLocationPermission
import com.example.rishabh_challenge.location.LocationService
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val photosAdapter = PhotosAdapter()
    private lateinit var viewModel: PhotosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            if(hasLocationPermission()) {
                val is_trekking = isServiceRunning(LocationService::class.java)
                if (!is_trekking) {
                    Intent(applicationContext, LocationService::class.java).apply {
                        action = LocationService.ACTION_START
                        startService(this)
                    }
                    binding.fab.setImageResource(R.drawable.stop)
                } else {
                    stopService(Intent(this, LocationService::class.java))
                    binding.fab.setImageResource(R.drawable.play)
                }
            }else{
                requestPermissions()
                Snackbar.make(view, "issing Location Permission", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

        val photosRecyclerView = findViewById<RecyclerView>(R.id.photosRecyclerView)
        photosRecyclerView.adapter = photosAdapter
        photosRecyclerView.layoutManager = GridLayoutManager(this, 1)
        val viewModelFactory = PhotosViewModel.TrekkingViewModelFactory(TrekkingRepository(applicationContext))
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PhotosViewModel::class.java)

        viewModel.photos.observe(this,
            Observer { list ->
                with(`photosAdapter`) {
                    photos.clear()
                    photos.addAll(list)
                    notifyDataSetChanged()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        val is_trekking = isServiceRunning(LocationService::class.java)
        if(!is_trekking){
            binding.fab.setImageResource(R.drawable.play)
        }else{
            binding.fab.setImageResource(R.drawable.stop)
        }
    }

    @Suppress("DEPRECATION") // Deprecated for third party Services.
    fun <T> Context.isServiceRunning(service: Class<T>) =
        (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == service.name }

    fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
    }

}