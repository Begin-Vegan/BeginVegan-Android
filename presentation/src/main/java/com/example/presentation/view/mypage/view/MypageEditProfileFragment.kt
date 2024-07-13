package com.example.presentation.view.mypage.view

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.main.MainNavigationHandler
import com.example.presentation.databinding.FragmentMypageEditProfileBinding
import com.example.presentation.view.main.MainFragment
import com.example.presentation.view.mypage.viewModel.MypageUserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MypageEditProfileFragment : BaseFragment<FragmentMypageEditProfileBinding>(R.layout.fragment_mypage_edit_profile) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val mypageUserInfoViewModel: MypageUserInfoViewModel by viewModels()

    private lateinit var emptyErrorText: String
    private lateinit var invalidErrorText: String

    override fun init() {
        backButton()
        setErrorText()
        setInputHelper()
        setObserve()
    }
    private fun setInputHelper() {
        binding.tilOnboardingEditNick.error = emptyErrorText
        mypageUserInfoViewModel.setValidNickName(false)

        mypageUserInfoViewModel.nickName.observe(this) { nickName ->
            binding.tilOnboardingEditNick.error = if (nickName.isNullOrEmpty()) {
                // 비어 있는 경우
                mypageUserInfoViewModel.setValidNickName(false)
                emptyErrorText
            } else {
                if (mypageUserInfoViewModel.validateNickName(nickName)) {
                    // 유효한 경우
                    mypageUserInfoViewModel.setValidNickName(true)
                    ""
                } else {
                    // 유효하지 않은 경우
                    mypageUserInfoViewModel.setValidNickName(false)
                    invalidErrorText
                }
            }
        }
    }
    private fun setErrorText() {
        emptyErrorText = getString(R.string.helper_text_nickname_default)
        invalidErrorText = getString(R.string.helper_text_nickname_check)
    }
    private fun setObserve() {
        mypageUserInfoViewModel.validNickName.observe(this) {
            binding.btnOnboardingNext.isEnabled = mypageUserInfoViewModel.checkValid()
            logMessage("${mypageUserInfoViewModel.nickName}")
        }
        mypageUserInfoViewModel.userInfoState.observe(this) { isUserInfoState ->
            if (isUserInfoState) {
//                navigateToMain()
            }else{
                showToast("유저 추가 정보 입력 오류")
            }
        }
    }


    private fun backButton(){
        binding.ibBackUp.setOnClickListener {
            mainNavigationHandler.popBackStack()
        }
    }
}