package com.example.domain.repository.review

import com.example.domain.model.core.BasicResult
import com.example.domain.model.review.RestaurantReview
import kotlinx.coroutines.flow.Flow

interface RestaurantReviewRepository {
    suspend fun getReviewByRestaurantId(
        reviewId: Long,
        page: Int,
        isPhoto: Boolean,
        filter: String
    ): Flow<List<RestaurantReview>>

    suspend fun writeReview(
        reviewId: Long,
        content: String,
        rate: Double,
        files: List<String?>
    ): Result<BasicResult>

    suspend fun reportReview(reviewId: Long, content: String): Result<BasicResult>
    suspend fun deleteReview(reviewId: Long): Result<BasicResult>
}