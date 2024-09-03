package com.example.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.data.model.device.FirstRunEntity
import com.example.data.model.map.HistorySearchEntity

@Database(
    entities = [HistorySearchEntity::class, FirstRunEntity::class], // 모든 엔티티를 포함합니다.
    version = 2,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class RoomDatabaseManager : RoomDatabase() {
    abstract fun historySearchDao(): HistorySearchDao
    abstract fun firstRunDao(): FirstRunDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabaseManager? = null

        fun getInstance(context: Context): RoomDatabaseManager {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabaseManager::class.java,
                    "beginvegan-database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
