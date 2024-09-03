package com.example.presentation.view.main.view

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.presentation.R
import com.example.presentation.base.BaseActivity
import com.example.presentation.config.navigation.MainNavigationHandler
import com.example.presentation.config.navigation.MainNavigationImpl
import com.example.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    lateinit var mainNavigationHandler: MainNavigationHandler
    lateinit var navController: NavController

    override fun initViewModel() {
    }

    override fun init() {
        binding.dlDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setNavController()
        setBottomNav()
        setupOnBackPressedCallback()
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcw_main_container) as NavHostFragment
        navController = navHostFragment.navController
        mainNavigationHandler = MainNavigationImpl(navController)
    }

    private fun setBottomNav() {
        with(binding) {
            bnvMain.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.mainHomeFragment || destination.id == R.id.veganMapFragment ||
                    destination.id == R.id.mainTipsFragment || destination.id == R.id.mainMypageFragment
                ) {
                    binding.bnvMain.visibility = View.VISIBLE
                } else binding.bnvMain.visibility = View.GONE
                when (destination.id) {
                    R.id.mainHomeFragment -> bnvMain.menu.findItem(R.id.item_home).isChecked = true
                    R.id.veganMapFragment -> bnvMain.menu.findItem(R.id.item_map).isChecked = true
                    R.id.mainTipsFragment -> bnvMain.menu.findItem(R.id.item_tips).isChecked = true
                    R.id.mainMypageFragment -> bnvMain.menu.findItem(R.id.item_profile).isChecked =
                        true
                }
            }

            bnvMain.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item_home -> mainNavigationHandler.navigateToHome()
                    R.id.item_map -> mainNavigationHandler.navigateToMap()
                    R.id.item_tips -> mainNavigationHandler.navigateToTips()
                    R.id.item_profile -> mainNavigationHandler.navigateToMypage()
                }
                true
            }

        }
    }

    private fun setupOnBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.dlDrawer.isDrawerOpen(GravityCompat.END)) {
                    binding.dlDrawer.closeDrawer(GravityCompat.END)
                } else if (navController.backQueue.size > 2) {
                    navController.popBackStack()
                } else {
                    isEnabled = false
                    finish()
                }
            }
        })
    }
}