package com.example.domain.useCase.review

import com.example.domain.model.core.BasicResult
import com.example.domain.model.review.RestaurantReview
import com.example.domain.repository.review.RestaurantReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestaurantReviewUseCase @Inject constructor(
    private val restaurantReview: RestaurantReviewRepository
) {
    // 식당 ID로 리뷰 가져오기
    suspend fun getReviewByRestaurantId(
        reviewId: Long,
        page: Int,
        isPhoto: Boolean,
        filter: String
    ): Flow<List<RestaurantReview>> =
        restaurantReview.getReviewByRestaurantId(reviewId, page, isPhoto, filter)

    // 리뷰 작성
    suspend fun writeReview(
        reviewId: Long,
        content: String,
        rate: Double,
        files: List<String?>
    ): Result<BasicResult> =
        restaurantReview.writeReview(reviewId, content, rate, files)

    // 리뷰 신고
    suspend fun reportReview(reviewId: Long, content: String): Result<BasicResult> =
        restaurantReview.reportReview(reviewId, content)

    // 리뷰 삭제
    suspend fun deleteReview(reviewId: Long): Result<BasicResult> =
        restaurantReview.deleteReview(reviewId)
}