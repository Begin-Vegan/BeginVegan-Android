package com.example.data.model.map

import com.squareup.moshi.Json

data class RecommendRestaurantResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<RecommendRestaurantInformation>
)

data class RecommendRestaurantInformation(
    @Json(name = "restaurantId")
    val restaurantId: Long,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "bookmark")
    val bookmark: Boolean,
    @Json(name = "latitude")
    val latitude: String,
    @Json(name = "longitude")
    val longitude: String
)