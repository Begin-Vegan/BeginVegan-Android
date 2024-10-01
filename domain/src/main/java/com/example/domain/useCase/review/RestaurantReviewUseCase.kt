package com.example.domain.useCase.review

import com.example.domain.model.review.RestaurantReview
import com.example.domain.repository.review.RestaurantReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestaurantReviewUseCase @Inject constructor(
    private val restaurantReview: RestaurantReviewRepository
) {
    // 식당 ID로 리뷰 가져오기
    suspend fun getReviewByRestaurantId(reviewId: Int): Flow<List<RestaurantReview>> =
        restaurantReview.getReviewByRestaurantId(reviewId = reviewId)

    // 리뷰 작성
    suspend fun writeReview(
        reviewId: Int,
        content: String,
        rate: Double,
        files: String?
    ): Result<Boolean> =
        restaurantReview.writeReview(reviewId, content, rate, files)

    // 리뷰 신고
    suspend fun reportReview(reviewId: Int): Result<Boolean> =
        restaurantReview.reportReview(reviewId)

    // 리뷰 삭제
    suspend fun deleteReview(reviewId: Int): Result<Boolean> =
        restaurantReview.deleteReview(reviewId)
}