package com.example.rishabh_challenge.db

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.plcoding.backgroundlocationtracking.networking.WebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TrekkingRepository(val context: Context) {

    fun getAllPhotos(id: String): LiveData<List<Photos>> {
        return TrekkingDatabase.getInstance(context).getTrekDao().getAllPhotos(id)
    }

    suspend fun getPhotoFromFlickr(lat: Double, long: Double){
        val searchResponse = WebClient.client.fetchImages(
            "d68faaf1c6e6930292a7848dbfb78d44",
            lat.toString(),
            long.toString())

        val photo = searchResponse.photos.photo[0];

        TrekkingDatabase.getInstance(context).getTrekDao().addPhoto(
            Photos(trek_id = 1,
                longitude = lat,
                latitude = long,
                url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg",
                timestamp = System.currentTimeMillis()
                )
        )
    }

    fun clearTrek(){
        try {
            CoroutineScope(Dispatchers.IO).launch {
                TrekkingDatabase.getInstance(context).getTrekDao().deleteAllPhotos()
            }
        } catch (e: Exception) {
            Log.d("ERROR", "ERROR IN CLEARING TREK")
        }
    }
}