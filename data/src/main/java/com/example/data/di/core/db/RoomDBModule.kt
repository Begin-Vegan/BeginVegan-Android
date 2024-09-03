package com.example.data.di.core.db

import android.content.Context
import com.example.data.mapper.map.HistorySearchMapper
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
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomDBModule {

    @Singleton
    @Provides
    fun provideHistorySearchDatabase(
        @ApplicationContext context: Context
    ): RoomDatabaseManager = RoomDatabaseManager.getInstance(context)

    @Provides
    @Singleton
    fun provideHistorySearchDao(database: RoomDatabaseManager): HistorySearchDao = database.historySearchDao()

    @Provides
    @Singleton
    fun provideHistorySearchRepository(
        historyDataSource: HistorySearchLocalDataSource,
        historySearchMapper: HistorySearchMapper
    ): HistorySearchRepository {
        return HistorySearchRepositoryImpl(historyDataSource, historySearchMapper)
    }

    @Provides
    @Singleton
    fun provideHistorySearchLocalDataSource(
        historySearchDao: HistorySearchDao
    ): HistorySearchLocalDataSource {
        return HistorySearchLocalDataSourceImpl(historySearchDao)
    }

    @Provides
    @Singleton
    fun provideHistorySearchMapper(): HistorySearchMapper {
        return HistorySearchMapper()
    }

    @Provides
    @Singleton
    fun provideFirstRunDao(database: RoomDatabaseManager): FirstRunDao = database.firstRunDao()

    @Provides
    @Singleton
    fun provideFirstRunDataSource(
        firstRunDao: FirstRunDao
    ): FirstRunDataSource {
        return FirstRunDataSourceImpl(firstRunDao)
    }

}
