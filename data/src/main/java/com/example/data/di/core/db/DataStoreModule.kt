package com.example.data.di.core.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.data.repository.local.auth.AuthTokenDataSource
import com.example.data.repository.local.auth.AuthTokenDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("auth_prefs")
        }
    }

    @Provides
    @Singleton
    fun provideAuthTokenDataSource(
        provideDataStore: DataStore<Preferences>
    ): AuthTokenDataSource {
        return AuthTokenDataSourceImpl(provideDataStore)
    }

}