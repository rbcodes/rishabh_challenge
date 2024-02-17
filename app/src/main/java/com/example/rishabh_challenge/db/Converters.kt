package com.example.rishabh_challenge.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.style.TtsSpan
import android.widget.Toast
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Converter class to save bitmaps in the Room Database
 */
class Converters {

    @TypeConverter
    fun toList(photos: String): List<String> {
        return photos.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(separator = ",")
    }

}