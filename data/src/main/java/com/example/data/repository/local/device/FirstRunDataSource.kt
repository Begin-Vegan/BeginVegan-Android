package com.example.data.repository.local.device

interface FirstRunDataSource {
    /**
     * 앱의 첫 실행 여부를 확인합니다.
     * @return true이면 첫 실행, false이면 첫 실행이 아님
     */
    suspend fun isFirstRun(): Boolean

    /**
     * 첫 실행이 완료되었음을 설정합니다.
     */
    suspend fun setFirstRunCompleted()
}