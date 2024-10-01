package com.example.data.di.review

import com.example.data.di.core.db.DataStoreModule
import com.example.data.di.core.network.NetworkModule
import com.example.data.mapper.core.BaseMapper
import com.example.data.mapper.review.ReviewMapper
import com.example.data.repository.local.auth.AuthTokenDataSource
import com.example.data.repository.remote.review.RestaurantReviewRemoteDataSource
import com.example.data.repository.remote.review.RestaurantReviewRemoteDataSourceImpl
import com.example.data.repository.remote.review.RestaurantReviewRemoteRepositoryImpl
import com.example.data.repository.remote.review.ReviewRecommendRemoteDataSource
import com.example.data.repository.remote.review.ReviewRecommendRepositoryImpl
import com.example.data.repository.remote.review.ReviewRecommnedRemoteDataSourceImpl
import com.example.data.retrofit.review.ReviewService
import com.example.domain.repository.review.RestaurantReviewRepository
import com.example.domain.repository.review.ReviewRecommendRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class ReviewModule {
    @Singleton
    @Provides
    fun provideReviewService(retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewRecommendRemoteDataSource(
        reviewService: ReviewService,
        authTokenDataSource: AuthTokenDataSource
    ): ReviewRecommendRemoteDataSource {
        return ReviewRecommnedRemoteDataSourceImpl(reviewService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideReviewRecommendRepository(
        reviewRecommendRemoteDataSource: ReviewRecommendRemoteDataSource
    ): ReviewRecommendRepository {
        return ReviewRecommendRepositoryImpl(reviewRecommendRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideReviewRemoteDataSource(
        reviewService: ReviewService,
        authTokenDataSource: AuthTokenDataSource
    ): RestaurantReviewRemoteDataSource {
        return RestaurantReviewRemoteDataSourceImpl(reviewService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideReviewRepository(
        restaurantReviewRemoteDataSource: RestaurantReviewRemoteDataSource,
        baseMapper: BaseMapper,
        reviewMapper: ReviewMapper
    ): RestaurantReviewRepository {
        return RestaurantReviewRemoteRepositoryImpl(
            restaurantReviewRemoteDataSource,
            baseMapper,
            reviewMapper
        )
    }


    @Provides
    fun provideReviewMapper(): ReviewMapper {
        return ReviewMapper()
    }

}