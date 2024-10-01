package com.example.data.di.core.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.mapper.map.HistorySearchMapper
import com.example.data.model.device.FirstRunEntity
import com.example.data.repository.local.device.FirstRunDataSource
import com.example.data.repository.local.device.FirstRunDataSourceImpl
import com.example.data.repository.local.search.HistorySearchLocalDataSource
import com.example.data.repository.local.search.HistorySearchLocalDataSourceImpl
import com.example.data.repository.local.search.HistorySearchRepositoryImpl
import com.example.data.room.FirstRunDao
import com.example.data.room.HistorySearchDao
import com.example.data.room.RoomDatabaseManager
import com.example.domain.repository.map.HistorySearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomDBModule {

    @Provides
    @Singleton
    fun provideRoomDatabaseManager(
        @ApplicationContext context: Context,
        firstRunCallback: RoomDatabase.Callback
    ): RoomDatabaseManager = Room.databaseBuilder(
        context,
        RoomDatabaseManager::class.java,
        "beginvegan-database.db"
    )
        .fallbackToDestructiveMigration()
        .addCallback(firstRunCallback) // 콜백 추가
        .build()

    @Provides
    fun provideFirstRunCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d("hilt","init first_run database insert")
                db.execSQL("INSERT INTO first_run (id, isFirstRun) VALUES(1, 1)")
            }
        }
    }
}