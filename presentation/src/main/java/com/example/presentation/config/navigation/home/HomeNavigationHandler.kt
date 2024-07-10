package com.example.presentation.config.navigation.home

import androidx.navigation.NavController

interface HomeNavigationHandler {
    fun navigateToHome()
    fun navigateToMap()
    fun navigateToTips(fromTest: Boolean)
    fun navigateToMypage()

    fun getNavController():NavController
}