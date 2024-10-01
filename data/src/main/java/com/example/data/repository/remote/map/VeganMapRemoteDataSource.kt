package com.example.data.repository.remote.map

import com.example.data.model.map.RecommendRestaurantResponse
import com.example.data.model.map.RestaurantDetailResponse
import com.example.data.model.map.VeganMapRestaurantResponse
import com.skydoves.sandwich.ApiResponse

interface VeganMapRemoteDataSource {
    suspend fun getNearRestaurantMap(
        page: Int,
        latitude: String,
        longitude: String
    ): ApiResponse<VeganMapRestaurantResponse>

    suspend fun getRestaurantDetail(
        restaurantId: Long,
        latitude: String,
        longitude: String
    ): ApiResponse<RestaurantDetailResponse>

    suspend fun getNearRestaurantWithPermission(
        count: Long,
        latitude: String,
        longitude: String
    ): ApiResponse<RecommendRestaurantResponse>

    suspend fun getNearRestaurantWithOutPermission(
        count: Long,
    ): ApiResponse<RecommendRestaurantResponse>
}