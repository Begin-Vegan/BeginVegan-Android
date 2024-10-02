package com.example.data.di.bookmarks

import com.example.data.di.core.db.DataStoreModule
import com.example.data.di.core.network.NetworkModule
import com.example.data.repository.local.auth.AuthTokenDataSource
import com.example.data.repository.remote.bookmarks.BookmarkRemoteDataSource
import com.example.data.repository.remote.bookmarks.BookmarkRemoteDataSourceImpl
import com.example.data.repository.remote.bookmarks.BookmarkRepositoryImpl
import com.example.data.retrofit.bookMarks.BookmarkService
import com.example.domain.repository.bookmarks.BookmarkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class BookmarkModule {
    @Singleton
    @Provides
    fun provideBookmarkService(retrofit: Retrofit): BookmarkService {
        return retrofit.create(BookmarkService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkRemoteDataSource(
        bookmarkService: BookmarkService,
        authTokenDataSource: AuthTokenDataSource
    ): BookmarkRemoteDataSource {
        return BookmarkRemoteDataSourceImpl(bookmarkService,authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(
        bookmarkRemoteDataSource: BookmarkRemoteDataSource
    ): BookmarkRepository {
        return BookmarkRepositoryImpl(bookmarkRemoteDataSource)
    }
}