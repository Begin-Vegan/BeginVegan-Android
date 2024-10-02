package com.example.data.di.fcm


import com.example.core_fcm.repository.FcmTokenRepository
import com.example.data.di.core.db.DataStoreModule
import com.example.data.di.core.network.NetworkModule
import com.example.data.repository.local.auth.AuthTokenDataSource
import com.example.data.repository.local.fcm.FcmTokenDataSource
import com.example.data.repository.remote.fcm.FcmRemoteDataSource
import com.example.data.repository.remote.fcm.FcmRemoteDataSourceImpl
import com.example.data.repository.remote.fcm.FcmTokenRepositoryImpl
import com.example.data.retrofit.fcm.FcmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class FcmModule{
    @Singleton
    @Provides
    fun provideFcmService(retrofit: Retrofit): FcmService {
        return retrofit.create(FcmService::class.java)
    }

    @Provides
    @Singleton
    fun provideFcmRemoteDataSource(fcmService: FcmService, authTokenDataSource: AuthTokenDataSource): FcmRemoteDataSource {
        return FcmRemoteDataSourceImpl(fcmService, authTokenDataSource)
    }

    @Singleton
    @Provides
    fun provideFcmTokenRepository(fcmRemoteDataSource: FcmRemoteDataSource, fcmTokenDataSource: FcmTokenDataSource): FcmTokenRepository {
        return FcmTokenRepositoryImpl(fcmRemoteDataSource, fcmTokenDataSource)
    }
}