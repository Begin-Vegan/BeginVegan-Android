package com.example.presentation.view.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.NearRestaurant
import com.example.presentation.R
import com.example.presentation.adapter.home.HomeRestaurantRVAdapter
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.MainNavController
import com.example.presentation.config.navigation.home.HomeNavigationHandler
import com.example.presentation.config.navigation.main.MainNavigationHandler
import com.example.presentation.databinding.FragmentMainHomeBinding
import com.example.presentation.config.navigation.tips.TipsNavigationHandler
import com.example.presentation.config.navigation.tips.TipsNavigationImpl
import com.example.presentation.util.DrawerController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentMainHomeBinding>(R.layout.fragment_main_home){
    private lateinit var homeRestaurantRVAdapter: HomeRestaurantRVAdapter

    @Inject
    lateinit var drawerController: DrawerController

    @Inject
    @MainNavController
    lateinit var mainNavigationHandler: MainNavigationHandler

    private lateinit var tipsNavigationHandler: TipsNavigationHandler

    // ViewModel 분리
    private var list: ArrayList<NearRestaurant> = ArrayList()

    override fun init() {
        setRestaurantRecyclerView()
        setTipsTab()
        setOpenDrawer()
        setBeganTest()
    }

    private fun setBeganTest() {
        binding.ivBannerVeganTest.setOnClickListener{
            mainNavigationHandler.navigateToBeganTest()
        }
    }

    private fun setOpenDrawer() {
        binding.includedToolbar.ibNotification.setOnClickListener {
            drawerController.openDrawer()
        }
    }

    private fun setTipsTab() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fcw_tips) as NavHostFragment
        val navController = navHostFragment.findNavController()
        tipsNavigationHandler = TipsNavigationImpl(navController)


        binding.tlTips.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> tipsNavigationHandler.navigateToMagazine()
                    1 -> tipsNavigationHandler.navigateToRecipe()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }



    // ViewModel 분리
    private fun testData(){
        list.add(NearRestaurant(0,"식당1","null"))
        list.add(NearRestaurant(0,"식당2","null"))
        list.add(NearRestaurant(0,"식당3","null"))
        list.add(NearRestaurant(0,"식당4","null"))
        list.add(NearRestaurant(0,"식당5","null"))
        list.add(NearRestaurant(0,"식당6","null"))
        list.add(NearRestaurant(0,"식당7","null"))
    }
    private fun setRestaurantRecyclerView() {
        testData()
        homeRestaurantRVAdapter = HomeRestaurantRVAdapter(requireContext())
        binding.rvRestaurantList.adapter = homeRestaurantRVAdapter
        homeRestaurantRVAdapter.submitList(list.toMutableList())
        binding.rvRestaurantList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    //Control Back Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnBackPressedCallback()
    }
    private fun setupOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.d("backButton is drawer open : ${drawerController.isDrawerOpen()}")
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