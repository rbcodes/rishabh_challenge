package com.example.rishabh_challenge.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrekDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertTrek(trek: Trek)

    @Query("SELECT * FROM trekking_table ORDER BY timestamp DESC")
    fun getAllTreks(): LiveData<List<Trek>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPhoto(photo: Photos)

    @Query("SELECT * FROM photos_table where trek_id == :id ORDER BY timestamp DESC")
    fun getAllPhotos(id: String): LiveData<List<Photos>>

    @Query("DELETE from photos_table")
    fun deleteAllPhotos()

}