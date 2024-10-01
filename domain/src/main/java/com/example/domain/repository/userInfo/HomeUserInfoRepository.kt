package com.example.domain.repository.userInfo

import com.example.domain.model.userInfo.HomeUserInfo
import kotlinx.coroutines.flow.Flow

interface HomeUserInfoRepository {
    suspend fun getHomeUserInfo(): Flow<HomeUserInfo>
}