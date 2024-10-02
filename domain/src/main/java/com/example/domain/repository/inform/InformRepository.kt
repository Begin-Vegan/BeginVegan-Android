package com.example.domain.repository.inform

import com.example.domain.model.core.BasicResult

interface InformRepository {
    suspend fun informNewRestaurant(name: String, location: String, content: String): Result<BasicResult>
    suspend fun informModifyRestaurant(restaurantId: Long, content: String): Result<BasicResult>
}