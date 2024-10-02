package com.example.data.repository.remote.fcm

import com.example.data.model.core.BaseResponse
import com.example.data.model.fcm.FcmMessageRequest
import com.example.data.model.fcm.HasFcmTokenResponse
import com.skydoves.sandwich.ApiResponse

interface FcmRemoteDataSource {
    suspend fun getHasFcmToken():ApiResponse<HasFcmTokenResponse>
    suspend fun patchFcmToken(token:String): ApiResponse<BaseResponse>
    suspend fun postFcmMessage(requestBody: FcmMessageRequest):ApiResponse<BaseResponse>
}