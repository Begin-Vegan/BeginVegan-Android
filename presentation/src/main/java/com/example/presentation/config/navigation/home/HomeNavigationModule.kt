//package com.example.presentation.config.navigation.home
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.navigation.NavController
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.fragment.findNavController
//import com.example.presentation.R
//import com.example.presentation.config.navigation.HomeNavController
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.android.components.FragmentComponent
//
//@Module
//@InstallIn(FragmentComponent::class)
//object HomeNavigationModule {
//    @Provides
//    @HomeNavController
//    fun provideHomeNavController(fragment: Fragment): NavController {
//        val navHostFragment = fragment.childFragmentManager.findFragmentById(R.id.fcw_home) as? NavHostFragment
//        return navHostFragment?.navController ?: throw IllegalStateException("NavHostFragment not found or not initialized")
//    }
//
//    @Provides
//    @HomeNavController
//    fun provideHomeNavigationHandler(@HomeNavController navController: NavController): HomeNavigationHandler {
//        return HomeNavigationImpl(navController)
//    }
//}