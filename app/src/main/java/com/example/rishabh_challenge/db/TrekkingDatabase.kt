package com.example.rishabh_challenge.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rishabh_challenge.utils.Singleton

@Database(
    entities = [Trek::class, Photos::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TrekkingDatabase : RoomDatabase() {

    abstract fun getTrekDao(): TrekDao

    companion object : Singleton<TrekkingDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, TrekkingDatabase::class.java, "trecking_db").build()
    })
}