package com.example.domain.model.review

data class RestaurantReview(
    val reviewId: Int,
    val user: Reviewer,
    val reviewType: String,
    val imageUrl: List<String>,
    val rate: Float,
    val date: String,
    val content: String,
    val visible: Boolean,
    val recommendationCount: Int,
    val recommendation: Boolean
)

data class Reviewer(
    val userId: Int,
    val imageUrl: String,
    val nickname: String,
    val userCode: String,
    val level: String
)
