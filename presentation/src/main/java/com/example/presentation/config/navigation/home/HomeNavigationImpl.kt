package com.example.presentation.config.navigation.home

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.presentation.R
import com.example.presentation.config.navigation.HomeNavController
import com.example.presentation.view.home.HomeFragmentDirections
import com.example.presentation.view.home.veganTest.view.VeganTestResultFragmentDirections
import com.example.presentation.view.tips.view.TipsFragmentArgs
import timber.log.Timber
import javax.inject.Inject

class HomeNavigationImpl (private val navController: NavController) : HomeNavigationHandler {

    override fun navigateToHome() {
        val currentId = navController.currentDestination?.id!!
        navController.navigate(R.id.mainHomeFragment, null,
            NavOptions.Builder().setPopUpTo(currentId, true).build())
    }

    override fun navigateToMap() {
        controlBackStack(R.id.veganMapFragment)
    }

    override fun navigateToTips(fromTest: Boolean) {
        if(fromTest){
            val action = HomeFragmentDirections.actionMainHomeFragmentToTipsFragment(fromTest)
            navController.navigate(action)
        }else{
            controlBackStack(R.id.tipsFragment)
//            navController.navigate(R.id.tipsFragment)
        }
    }

    override fun navigateToMypage() {
        controlBackStack(R.id.mainMypageFragment)
//        navController.navigate(R.id.mainMypageFragment)
    }

    override fun getNavController(): NavController {
        return navController
    }

    private fun controlBackStack(destinationId:Int){
        val currentId = navController.currentDestination?.id!!
        if(currentId == R.id.mainHomeFragment){
            navController.navigate(destinationId)
        }else{
            navController.navigate(destinationId, null,
                NavOptions.Builder().setPopUpTo(currentId, true).build())
        }
    }
}