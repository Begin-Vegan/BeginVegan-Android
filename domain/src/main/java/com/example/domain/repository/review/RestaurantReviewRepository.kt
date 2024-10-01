package com.example.domain.repository.review

import com.example.domain.model.review.RestaurantReview
import kotlinx.coroutines.flow.Flow

interface RestaurantReviewRepository {
    suspend fun getReviewByRestaurantId(reviewId: Int): Flow<List<RestaurantReview>>
    suspend fun writeReview(
        reviewId: Int,
        content: String,
        rate: Double,
        files: String?
    ): Result<Boolean>

    suspend fun reportReview(reviewId: Int): Result<Boolean>
    suspend fun deleteReview(reviewId: Int): Result<Boolean>
}