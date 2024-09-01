package com.example.presentation.view.home.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.map.VeganMapRestaurant
import com.example.presentation.R
import com.example.presentation.adapter.home.HomeRestaurantRVAdapter
import com.example.presentation.base.BaseFragment
import com.example.presentation.config.navigation.MainNavigationHandler
import com.example.presentation.databinding.FragmentMainHomeBinding
import com.example.presentation.util.DrawerController
import com.example.presentation.util.PermissionDialog
import com.example.presentation.view.main.MainViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentMainHomeBinding>(R.layout.fragment_main_home) {
    private lateinit var homeRestaurantRVAdapter: HomeRestaurantRVAdapter
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)

    @Inject
    lateinit var drawerController: DrawerController

    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private var tipsNowTab = "MAGAZINE"

    private var list: ArrayList<VeganMapRestaurant> = ArrayList()

    private val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    private lateinit var locationListener: LocationListener

    private lateinit var locationManager: LocationManager

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            val isFineLocation = isGranted[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val isCoarseLocation = isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            when {
                isFineLocation && isCoarseLocation -> {
                    // FineLoaction 승인 시, CoarseLoaction 자동 승인
                    // 정확한 위치 권한 승인
                    logMessage("locationPermissionLauncher Fine Location, Coarse Location Granted 정확한 위치 권한 승인")
                    getLocation()
                    getFineLocation()
                }

                !isFineLocation && isCoarseLocation -> {
                    // 대략적인 위치 권한 승인
                    logMessage("locationPermissionLauncher Only Coarse Location Granted 대략적인 위치 권한 승인")
                    getLocation()
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            ACCESS_FINE_LOCATION
                        )
                    ) {
                        logMessage("locationPermissionLauncher Fine Location 거부 경험 있음")
                        showPermissionDeniedDialog()
                    } else {
                        logMessage("locationPermissionLauncher Fine Location 거부 경험 없음")
                        showFineLocationDialog()
                    }

                }

                else -> {
                    // 위치 권한 승인하지 않음
                    logMessage("locationPermissionLauncher Permission Denied 위치 권한 거부")
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            ACCESS_COARSE_LOCATION
                        )
                    ) {
                        logMessage("locationPermissionLauncher 위치 권한 거부 경험 없음")
                        showPermissionRationaleDialog()
                    } else {
                        logMessage("locationPermissionLauncher 위치 권한 거부 경험 있음")
                        showPermissionDeniedDialog()
                    }
                }
            }


        }


    override fun init() {
        binding.lifecycleOwner = this

        setUserInfo()

        setTipsTab()

        setOpenDrawer()

        setBeganTest()

        checkAndRequestPermissions()
    }

    private fun setUserInfo() {
    }


    private fun setBeganTest() {
        binding.ivBannerVeganTest.setOnClickListener {
            mainNavigationHandler.navigateHomeToVeganTest()
        }
    }

    private fun setOpenDrawer() {
        binding.includedToolbar.ibNotification.setOnClickListener {
            drawerController.openDrawer()
        }
    }

    private fun getLocation() {
        locationManager = ContextCompat.getSystemService(
            requireContext(),
            LocationManager::class.java
        ) as LocationManager

        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        location?.let {
            val latitude = location.latitude
            val longitude = location.longitude
            val accuracy = location.accuracy
            val time = location.time
            logMessage("getLocation\nlatitude = $latitude,\nlongitude = $longitude\nlocation = $location,\naccuracy = $accuracy,\ntime = $time")
        }
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 위치 정보가 변경될 때 호출되는 콜백
                logMessage("onLocationChanged")
                logMessage("${location.latitude} ${location.latitude}")
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // 위치 제공자 상태 변경 시 호출되는 콜백
                logMessage("onStatusChanged")
            }

            override fun onProviderEnabled(provider: String) {
                // 위치 제공자가 사용 가능할 때 호출되는 콜백
                logMessage("onProviderEnabled")
            }

            override fun onProviderDisabled(provider: String) {
                // 위치 제공자가 사용 불가능할 때 호출되는 콜백
                logMessage("onProviderDisabled")
            }
        }
    }

    private fun getFineLocation() {
//        try {
//            logMessage("getFineLocation granted")
//            locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                5000L, // 5초
//                10f, // 10미터,
//                locationListener
//            )
//        } catch (e: SecurityException) {
//            logMessage("Location permission not granted")
//            showPermissionDeniedDialog()
//        }
    }

    private fun setTipsTab() {
        replaceFragment(HomeTipsMagazineFragment())
        tipsNowTab = "MAGAZINE"

        binding.tlTips.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceFragment(HomeTipsMagazineFragment())
                        tipsNowTab = "MAGAZINE"
                    }

                    1 -> {
                        replaceFragment(HomeTipsRecipeFragment())
                        tipsNowTab = "RECIPE"
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        setTipsMoreButton()
    }

    private fun setTipsMoreButton() {
        binding.btnTipsMore.setOnClickListener {
            when (tipsNowTab) {
                "MAGAZINE" -> {
                    mainNavigationHandler.navigateToTips()
                }

                "RECIPE" -> {
                    mainViewModel.setTipsMoveToRecipe(true)
                    mainNavigationHandler.navigateToTips()
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fl_tips_content, fragment)
            .commit()
    }



    private fun checkAndRequestPermissions() {
        when {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                logMessage("checkAndRequestPermissions 정확한 위치 권한 승인")
                getLocation()
            }

            ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                logMessage("checkAndRequestPermissions 대략적인 위치 권한 승인")
                getLocation()
            }

            else -> {
                logMessage("checkAndRequestPermissions 위치 권한 없음")
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        ACCESS_COARSE_LOCATION
                    )
                ) {
                    logMessage(
                        "shouldShowRequestPermissionRationale = true"
                    )
                } else {
                    logMessage(
                        "shouldShowRequestPermissionRationale = false"
                    )
                    locationPermissionLauncher.launch(permissions)
                }

            }
        }
    }

    //     권한 재요청
    private fun showPermissionRationaleDialog() {
        var isRetry = false
        PermissionDialog.Builder()
            .setTitle("권한 재요청 안내")
            .setBody(
                "해당 권한을 거부할 경우, 다음 기능의 사용이 불가능해요." +
                        "\n · Map 기능 전체 "
            )
            .setPositiveButton("권한재요청") {
                isRetry = true
                locationPermissionLauncher.launch(permissions)
            }.setNegativeButton("닫기") {
                logMessage("닫기")
            }
            .setOnDismissListener {
                if (!isRetry) {
                    showPermissionDeniedDialog()
                }
            }
            .show(childFragmentManager, "showPermissionRationaleDialog")
    }

    // 권한 허용 안함
    private fun showPermissionDeniedDialog() {
        PermissionDialog.Builder()
            .setTitle("기능 사용 불가 안내")
            .setBody(
                "위치 정보에 대한 권한 사용을 거부하셨어요.\n" +
                        "\n" +
                        "기능 사용을 원하실 경우 [휴대폰 설정 > 애플리케이션 관리자]에서 해당 앱의 권한을 허용해 주세요."
            )
            .setPositiveButton("확인") {
                logMessage("showPermissionDeniedDialog 확인")
            }.show(childFragmentManager, "showPermissionDeniedDialog")
    }

    private fun showFineLocationDialog() {
        val dialog = PermissionDialog.Builder()
            .setTitle("정확한 위치 권한 요청 안내")
            .setBody(
                "Map 메뉴는 '정확한 위치' 권한으로만 사용 가능합니다.\n" +
                        "'정확한 위치' 사용 권한을 허용해 주세요."
            )
            .setPositiveButton("설정") {
                locationPermissionLauncher.launch(permissions)
            }.setNegativeButton("닫기") {
                showPermissionDeniedDialog()
                logMessage("showFineLocationDialog 닫기")
            }.show(childFragmentManager, "showFineLocationDialog")
    }

    companion object {
        private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

}