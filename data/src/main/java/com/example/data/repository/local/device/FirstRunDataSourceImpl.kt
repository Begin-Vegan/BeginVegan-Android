package com.example.data.repository.local.device

import com.example.data.model.device.FirstRunEntity
import com.example.data.room.FirstRunDao
import javax.inject.Inject

class FirstRunDataSourceImpl @Inject constructor(
    private val firstRunDao: FirstRunDao
) : FirstRunDataSource {

    override suspend fun isFirstRun(): Boolean {
        return firstRunDao.isFirstRun()
    }

    override suspend fun setFirstRunCompleted() {
        firstRunDao.insertFirstRun(FirstRunEntity(isFirstRun = false))
    }
}