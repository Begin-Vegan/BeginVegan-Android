package com.example.data.di.device

import com.example.data.di.core.db.DataStoreModule
import com.example.data.di.core.network.NetworkModule
import com.example.data.mapper.map.HistorySearchMapper
import com.example.data.repository.local.device.FirstRunDataSource
import com.example.data.repository.local.device.FirstRunDataSourceImpl
import com.example.data.repository.local.device.FirstRunRepositoryImpl
import com.example.data.repository.local.search.HistorySearchLocalDataSource
import com.example.data.repository.local.search.HistorySearchRepositoryImpl
import com.example.data.room.FirstRunDao
import com.example.data.room.RoomDatabaseManager
import com.example.domain.repository.device.FirstRunRepository
import com.example.domain.repository.map.HistorySearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DeviceModule {

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

    @Provides
    @Singleton
    fun provideFirstRunRepository(
        firstRunDataSource: FirstRunDataSource
    ): FirstRunRepository {
        return FirstRunRepositoryImpl(firstRunDataSource)
    }
}