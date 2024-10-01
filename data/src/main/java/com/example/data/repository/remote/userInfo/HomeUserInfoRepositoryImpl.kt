package com.example.data.repository.remote.userInfo

import androidx.datastore.preferences.protobuf.Api
import com.example.data.mapper.userInfo.HomeUserInfoMapper
import com.example.domain.model.userInfo.HomeUserInfo
import com.example.domain.repository.userInfo.HomeUserInfoRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class HomeUserInfoRepositoryImpl @Inject constructor(
    private val homeUserInfoDataSource: HomeUserInfoDataSource,
    private val userInfoMapper: HomeUserInfoMapper
) : HomeUserInfoRepository {
    override suspend fun getHomeUserInfo(): Flow<HomeUserInfo> {
        return flow {
            try {
                when (val response = homeUserInfoDataSource.getHomeUserInfo()) {
                    is ApiResponse.Success -> {
                        Timber.e("Success fetching user info: $response")
                        val userInfo = userInfoMapper.mapFromEntity(response.data.information)
                        emit(userInfo)
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("Error fetching user info: ${response.errorBody}")
                        throw Exception("Error fetching user info: ${response.errorBody}")
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("Exception fetching user info: ${response.message}")
                        throw Exception("Exception fetching user info: ${response.message}")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception in getHomeUserInfo")
                throw e
            }
        }
    }
}