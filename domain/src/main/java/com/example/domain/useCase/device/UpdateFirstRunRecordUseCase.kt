package com.example.domain.useCase.device

import com.example.domain.repository.device.FirstRunRepository
import javax.inject.Inject

class UpdateFirstRunRecordUseCase @Inject constructor(
    private val firstRunRepository: FirstRunRepository
){
    suspend operator fun invoke() {
        firstRunRepository.updateFirstRunRecord()
    }
}