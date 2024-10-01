package com.example.domain.useCase.device

import com.example.domain.repository.device.FirstRunRepository
import javax.inject.Inject

class IsFirstRunUseCase @Inject constructor(
    private val firstRunRepository: FirstRunRepository
){
    suspend operator fun invoke(): Boolean {
        return firstRunRepository.isCheckFirstRun()
    }
}