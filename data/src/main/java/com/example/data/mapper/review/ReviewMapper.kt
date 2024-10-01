package com.example.data.mapper.review

import com.example.data.model.review.RestaurantReviewResponse
import com.example.data.model.review.Review
import com.example.domain.mapper.Mapper
import com.example.domain.model.review.RestaurantReview
import com.example.domain.model.review.Reviewer
import com.kakao.sdk.user.model.User

class ReviewMapper : Mapper<List<Review>, List<RestaurantReview>> {
    override fun mapFromEntity(type: List<Review>): List<RestaurantReview> {
        return type.map {
            RestaurantReview(
                reviewId = it.reviewId,
                user = it.user as Reviewer,
                reviewType = it.reviewType,
                imageUrl = it.imageUrl,
                rate = it.rate,
                date = it.date,
                content = it.content,
                visible = it.visible,
                recommendationCount = it.recommendationCount,
                recommendation = it.recommendation
            )
        }
    }
}