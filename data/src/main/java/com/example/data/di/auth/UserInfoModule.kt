package com.example.data.di.auth

import com.example.data.di.core.db.DataStoreModule
import com.example.data.di.core.network.NetworkModule
import com.example.data.mapper.core.BaseMapper
import com.example.data.mapper.userInfo.HomeUserInfoMapper
import com.example.data.repository.local.auth.AuthTokenDataSource
import com.example.data.repository.remote.userInfo.HomeUserInfoDataSource
import com.example.data.repository.remote.userInfo.HomeUserInfoDataSourceImpl
import com.example.data.repository.remote.userInfo.HomeUserInfoRepositoryImpl
import com.example.data.repository.remote.userInfo.SaveUserInfoDataSource
import com.example.data.repository.remote.userInfo.SaveUserInfoDataSourceImpl
import com.example.data.repository.remote.userInfo.SaveUserInfoRepositoryImpl
import com.example.data.retrofit.auth.UserInfoService
import com.example.domain.repository.userInfo.HomeUserInfoRepository
import com.example.domain.repository.userInfo.SaveUserInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
object UserInfoModule {
    @Singleton
    @Provides
    fun providerUserInfoService(retrofit: Retrofit): UserInfoService {
        return retrofit.create(UserInfoService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserInfoRemoteDataSource(
        userServiceInfo: UserInfoService,
        authTokenDataSource: AuthTokenDataSource
    ): SaveUserInfoDataSource {
        return SaveUserInfoDataSourceImpl(userServiceInfo, authTokenDataSource)
    }

    @Provides
    fun provideAuthRepository(
        saveUserInfoDataSource: SaveUserInfoDataSource,
        baseMapper: BaseMapper
    ): SaveUserInfoRepository {
        return SaveUserInfoRepositoryImpl(saveUserInfoDataSource, baseMapper)
    }

    @Provides
    fun provideHomeUserInfoRemoteDataSource(
        homeUserInfoService: UserInfoService,
        authTokenDataSource: AuthTokenDataSource
    ): HomeUserInfoDataSource {
        return HomeUserInfoDataSourceImpl(homeUserInfoService, authTokenDataSource)
    }

    @Provides
    fun provideHomeUserInfoRepository(
        homeUserInfoDataSource: HomeUserInfoDataSource,
        userInfoMapper: HomeUserInfoMapper
    ): HomeUserInfoRepository {
        return HomeUserInfoRepositoryImpl(homeUserInfoDataSource, userInfoMapper)
    }

    @Provides
    fun provideHomeUserInfoMapper(): HomeUserInfoMapper {
        return HomeUserInfoMapper()
    }
}