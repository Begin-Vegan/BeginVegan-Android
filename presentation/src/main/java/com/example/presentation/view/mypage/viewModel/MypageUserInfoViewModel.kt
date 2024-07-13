package com.example.presentation.view.mypage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.mypage.MypageUserInfo
import com.example.domain.useCase.mypage.MypageUserInfoUseCase
import com.example.domain.useCase.veganType.PatchVeganTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MypageUserInfoViewModel @Inject constructor(
    private val mypageUserInfoUseCase: MypageUserInfoUseCase,
    private val patchVeganTypeUseCase: PatchVeganTypeUseCase
):ViewModel(){

    private val _userInfo = MutableLiveData<MypageUserInfo>()
    val userInfo:LiveData<MypageUserInfo> = _userInfo

    val nickName = MutableLiveData<String?>()
    private var _validNickName = MutableLiveData<Boolean>()
    private var _validVeganLevel = MutableLiveData<Boolean>()

    // 이름, 비건 레벨 유효성 검사 LiveData
    val validNickName: LiveData<Boolean> = _validNickName
    val validVeganLevel: LiveData<Boolean> = _validVeganLevel

    private val _userInfoState = MutableLiveData<Boolean>(false)
    val userInfoState: LiveData<Boolean> get() = _userInfoState

    init {
        getUserInfo()
    }
    private fun getUserInfo(){
        viewModelScope.launch {
            mypageUserInfoUseCase.invoke().onSuccess {
                _userInfo.value = it
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }
    fun patchUserVeganType(veganType:String){
        viewModelScope.launch {
            patchVeganTypeUseCase.invoke("MYPAGE",veganType).onSuccess {
                Timber.d("patchUserVeganType onSuccess")
            }.onFailure {
                Timber.e("patchUserVeganType onFailure")
            }
        }
    }

    //edit profile
    fun setValidNickName(check: Boolean) {
        _validNickName.value = check
    }
    fun validateNickName(input: String): Boolean {
        // 입력이 2~12자인지 확인
        if (input.length !in 2..12) {
            return false
        }

        // 입력이 한글 또는 영문으로 이루어져 있는지 확인
        if (!input.all { it in '가'..'힣' || it in 'a'..'z' || it in 'A'..'Z' }) {
            return false
        }

        // 입력에 공백이 포함되어 있는지 확인
        if (input.contains(" ")) {
            return false
        }

        // 모든 조건을 만족하면 true 반환
        return true
    }
    fun checkValid(): Boolean {
        return validNickName.value ?: false && validVeganLevel.value ?: false
    }
}