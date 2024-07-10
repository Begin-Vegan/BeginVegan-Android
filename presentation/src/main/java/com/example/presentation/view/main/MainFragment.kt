package com.example.presentation.view.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.HomeNavController
import com.example.presentation.config.navigation.home.HomeNavigationHandler
import com.example.presentation.config.navigation.home.HomeNavigationImpl
import com.example.presentation.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
//    @Inject
//    @HomeNavController
    private lateinit var homeNavigationHandler: HomeNavigationHandler
//    private lateinit var navController:NavController

    override fun init() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fcw_home) as NavHostFragment
        val navController = navHostFragment.findNavController()
        homeNavigationHandler = HomeNavigationImpl(navController)
        checkFromTest()

        with(binding) {
            bnvMain.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.mainHomeFragment -> bnvMain.menu.findItem(R.id.item_home).isChecked = true
                    R.id.veganMapFragment -> bnvMain.menu.findItem(R.id.item_map).isChecked = true
                    R.id.tipsFragment -> bnvMain.menu.findItem(R.id.item_tips).isChecked = true
                    R.id.mainMypageFragment -> bnvMain.menu.findItem(R.id.item_profile).isChecked = true
                }
            }

            bnvMain.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item_home -> homeNavigationHandler.navigateToHome()
                    R.id.item_map ->  homeNavigationHandler.navigateToMap()
                    R.id.item_tips -> homeNavigationHandler.navigateToTips(false)
                    R.id.item_profile -> homeNavigationHandler.navigateToMypage()
                }
                true
            }
        }
    }

    private fun checkFromTest(){
        val args: MainFragmentArgs by navArgs()
        if(args.fromTest){
            homeNavigationHandler.navigateToTips(true)
        }
    }
}