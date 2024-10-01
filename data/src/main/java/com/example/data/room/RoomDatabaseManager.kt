package com.example.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.data.model.device.FirstRunEntity
import com.example.data.model.map.HistorySearchEntity

@Database(
    entities = [HistorySearchEntity::class, FirstRunEntity::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class RoomDatabaseManager : RoomDatabase() {
    abstract fun historySearchDao(): HistorySearchDao
    abstract fun firstRunDao(): FirstRunDao
}
