package com.example.domain.useCase.inform

import com.example.domain.model.core.BasicResult
import com.example.domain.repository.inform.InformRepository
import javax.inject.Inject

class InformUseCase @Inject constructor(
    private val informRepository: InformRepository
) {
    suspend fun informNewRestaurant(
        name: String,
        location: String,
        content: String
    ): Result<BasicResult> = informRepository.informNewRestaurant(name, location, content)

    suspend fun informModifyRestaurant(restaurantId: Long, content: String): Result<BasicResult> =
        informRepository.informModifyRestaurant(restaurantId, content)
}