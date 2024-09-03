package com.example.data.repository.local.device

import com.example.data.model.device.FirstRunEntity
import com.example.data.room.FirstRunDao
import javax.inject.Inject

class FirstRunDataSourceImpl @Inject constructor(
    private val firstRunDao: FirstRunDao
) : FirstRunDataSource {

    override suspend fun isFirstRun(): Boolean {
        val firstRun = firstRunDao.getFirstRun()
        return firstRun == null || firstRun.isFirstRun
    }

    override suspend fun setFirstRunCompleted() {
        firstRunDao.insertFirstRun(FirstRunEntity(isFirstRun = false))
    }
}