package com.example.rishabh_challenge.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "trekking_table")
data class Trek(
    val url: String,
    var timestamp: Long = 0L,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

@Entity(tableName = "photos_table")
data class Photos(
    val trek_id: Int,
    val url: String,
    val latitude: Double,
    val longitude: Double,
    var timestamp: Long = 0L,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}