package com.example.presentation.config.navigation.main

import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.presentation.R
import com.example.presentation.config.navigation.MainNavController
import com.example.presentation.view.home.veganTest.view.VeganTestResultFragmentDirections
import timber.log.Timber
import javax.inject.Inject

class MainNavigationImpl @Inject constructor(
    @MainNavController private val navController: NavController
) : MainNavigationHandler {

    override fun navigateToEditProfile() {
        navController.navigate(R.id.action_mainFragment_to_mypageEditProfileFragment)
    }

    override fun navigateToMyRestaurant() {
        navController.navigate(R.id.action_mainFragment_to_mypageMyRestaurantFragment)
    }

    override fun navigateToMyMagazine() {
        navController.navigate(R.id.action_mainFragment_to_mypageMyMagazineFragment)
    }

    override fun navigateToMyRecipe() {
        navController.navigate(R.id.action_mainFragment_to_mypageMyRecipeFragment)
    }

    override fun navigateToMySetting() {
        navController.navigate(R.id.action_mainFragment_to_mypageSettingFragment)
    }

    override fun navigateToReview() {
        navController.navigate(R.id.action_mainFragment_to_mypageMyReviewFragment)
    }

    override fun navigateToBeganTest() {
        navController.navigate(R.id.action_mainFragment_to_veganTestFragment)
    }

    override fun navigateToBeganTestResult() {
        navController.navigate(R.id.action_veganTestFragment_to_veganTestResultFragment)
    }

    override fun navigateToTipsMagazineDetail() {
        navController.navigate(R.id.action_mainFragment_to_tipsMagazineDetailFragment)
    }

    override fun navigateToMainHome(fromTest: Boolean) {
        val action = VeganTestResultFragmentDirections.actionVeganTestResultFragmentToMainFragment(fromTest)
        navController.navigate(action)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun navController():NavController {
        return navController
    }
}