package com.example.data.di.map

import com.example.data.di.core.db.DataStoreModule
import com.example.data.mapper.map.HistorySearchMapper
import com.example.data.repository.local.search.HistorySearchLocalDataSource
import com.example.data.repository.local.search.HistorySearchLocalDataSourceImpl
import com.example.data.repository.local.search.HistorySearchRepositoryImpl
import com.example.data.room.HistorySearchDao
import com.example.data.room.RoomDatabaseManager
import com.example.domain.repository.map.HistorySearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class VeganMapSearchModule {
    @Provides
    @Singleton
    fun provideHistorySearchDao(database: RoomDatabaseManager): HistorySearchDao =
        database.historySearchDao()

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
}