package com.example.presentation.view.mypage.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.MainNavController
import com.example.presentation.config.navigation.main.MainNavigationHandler
import com.example.presentation.databinding.FragmentMainMypageBinding
import com.example.presentation.util.DrawerController
import com.example.presentation.util.MypageUserLevelExplainDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


////import com.bumptech.glide.Glide
//import com.example.beginvegan.R
//import com.example.beginvegan.config.ApplicationClass
//import com.example.beginvegan.config.BaseFragment
//import com.example.beginvegan.databinding.FragmentMainMypageBinding
//import com.example.beginvegan.util.LogoutDialog
//import com.example.beginvegan.util.ProfileEditNameDialog
//import com.example.beginvegan.util.ProfileEditVeganTypeDialog
//import com.example.presentation.base.BaseFragment
//
//class MainMypageFragment : BaseFragment<FragmentMainMypageBinding>(
//    FragmentMainMypageBinding::bind, R.layout.fragment_main_mypage
//), BottomSheetLogoutFragment.MyFragmentInteractionListener,
//    ProfileEditNameDialog.EditNameDialogListener,
//    ProfileEditVeganTypeDialog.EditVeganTypeDialogListener {
//    override fun init() {
////
////        //ViewPager
////        val vpMyRecords = binding.vpMyRecords
////        vpMyRecords.adapter = ProfileMyRecordsVPAdapter(this)
////        //tab
////        val tabLayout = binding.tlRecords
////        TabLayoutMediator(tabLayout, vpMyRecords) { tab, pos ->
////            when (pos) {
////                0 -> tab.text = "나의 리뷰"
////                1 -> tab.text = "나의 스크랩"
////            }
////        }.attach()
////        //시작 시 유저 이름 반영
////        binding.tvUsername.text = ApplicationClass.xAuth.name
////
////        //닉네임 수정 dialog
////        binding.ibEditUsername.setOnClickListener {
////            openEditUserNameDialog()
////        }
////        //Vegan Type 수정 dialog
////        binding.ibEditVeganType.setOnClickListener {
////            openEditVeganTypeDialog()
////        }
////        //테스트 바로가기
////        binding.bGoVeganTest.setOnClickListener {
////            val intent = Intent(this.context, VeganTestActivity::class.java)
////            startActivity(intent)
////        }
////        //로그아웃 more button 클릭
////        binding.btnProfileMore.setOnClickListener {
////            openBottomSheetLogout()
////        }
////        binding.tvUsername.text = ApplicationClass.xAuth.name
//////        binding.tvUserVeganType.text = VeganTypes.valueOf("${ApplicationClass.xAuth.veganType}").veganType
////        if (ApplicationClass.xAuth.imageUrl != null) {
////            Glide.with(requireContext()).load(ApplicationClass.xAuth.imageUrl)
////                .into(binding.civProfileImage)
////        }
//    }
////
//    //닉네임 수정
////    private fun openEditUserNameDialog() { //dialog 띄우기
////        val editNameDialog =
////            ProfileEditNameDialog(requireContext(), binding.tvUsername.text.toString())
////        editNameDialog.setListener(this)
////        editNameDialog.show()
////    }
//
//    override fun editNameOnSaveClicked(name: String) { //수정한 name UI 반영
//        ApplicationClass.xAuth.name = name //이름 변경
////        binding.tvUsername.text = name
//    }
////
//    //비건 유형 수정
////    private fun openEditVeganTypeDialog() { //dialog 띄우기
////        val editVeganTypeDialog =
////            ProfileEditVeganTypeDialog(requireContext(), binding.tvUserVeganType.text.toString())
////        editVeganTypeDialog.setListener(this)
////        editVeganTypeDialog.show()
////    }
////
//    override fun editVeganTypeOnSaveClicked(type: String) {
////        binding.tvUserVeganType.text = type
//    }
//
////    //로그아웃
////    private fun openBottomSheetLogout() { //bottom sheet 열기
////        val bottomSheet = BottomSheetLogoutFragment()
////        bottomSheet.listener = this
////        bottomSheet.show(requireActivity().supportFragmentManager, null)
////    }
//
//        fun openLogoutDialog() { //dialog 띄우기
//            val logoutDialog = LogoutDialog(requireContext())
//            logoutDialog.show()
//        }
//
//    override fun onButtonClicked() { //dialog 로그아웃 버튼 클릭
//        openLogoutDialog()
//    }
//}

@AndroidEntryPoint
class MainMypageFragment : BaseFragment<FragmentMainMypageBinding>(R.layout.fragment_main_mypage) {

    @Inject
    lateinit var drawerController: DrawerController

    @Inject
    @MainNavController
    lateinit var mainNavigationHandler: MainNavigationHandler

    override fun init() {
        binding.llUserLevelExplain.setOnClickListener {
            openDialogUserLevelExplain()
        }

        setOpenDrawer()
        setProgressBar(5, 1)
        setVeganTypeDropdown(getString(R.string.vegan_type_unknown))



        binding.llEditProfile.setOnClickListener {
            mainNavigationHandler.navigateToEditProfile()
        }
        binding.llMyReview.setOnClickListener {
            mainNavigationHandler.navigateToReview()
        }
        binding.llMyRestaurant.setOnClickListener {
            mainNavigationHandler.navigateToMyRestaurant()
        }
        binding.llMyMagazine.setOnClickListener {
            mainNavigationHandler.navigateToMyMagazine()
        }
        binding.llMyRecipe.setOnClickListener {
            mainNavigationHandler.navigateToMyRecipe()
        }
        binding.llSetting.setOnClickListener {
            mainNavigationHandler.navigateToMySetting()
        }
    }

    private fun setOpenDrawer() {
        binding.includedToolbar.ibNotification.setOnClickListener {
            drawerController.openDrawer()
        }
    }

    private fun openDialogUserLevelExplain() {
        val dialog = MypageUserLevelExplainDialog(requireContext())
        dialog.show()
    }

    private fun setProgressBar(maxInt: Int, nowGauge: Int) {
        binding.pbUserLevelExp.max = maxInt
        binding.pbUserLevelExp.progress = nowGauge
    }

    private fun setVeganTypeDropdown(userVeganType: String) {
        val dropdownAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_mypage_set_vegan_type,
            resources.getStringArray(R.array.vegan_type)
        )
        binding.acsSetVeganType.adapter = dropdownAdapter
//        binding.acsSetVeganType.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View,
//                    position: Int,
//                    id: Long
//                ) {
//                    // 선택됐을 경우
//
//                }
//                override fun onNothingSelected(parent: AdapterView<*>) {}
//            }
//        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.sSetVeganType.adapter = dropdownAdapter
    }


    //Control Back Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnBackPressedCallback()
    }
    private fun setupOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerController.isDrawerOpen()) {
                    drawerController.closeDrawer()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}