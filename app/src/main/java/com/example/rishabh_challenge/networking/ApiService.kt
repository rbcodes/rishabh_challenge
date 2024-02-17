package com.plcoding.backgroundlocationtracking.networking

import com.plcoding.backgroundlocationtracking.data.PhotosSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&per_page=1")
    suspend fun fetchImages(@Query("api_key") FLICKR_API_KEY:String,
                            @Query("lat") lat:String,
                            @Query("lon") long:String): PhotosSearchResponse
}