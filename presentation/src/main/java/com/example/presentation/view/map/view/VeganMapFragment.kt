package com.example.presentation.view.map.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.map.VeganMapRestaurant
import com.example.presentation.R
import com.example.presentation.base.BaseFragment
import com.example.presentation.databinding.FragmentMainMapBinding
import com.example.presentation.util.PermissionDialog
import com.example.presentation.util.RestaurantReportDialog
import com.example.presentation.view.map.adapter.VeganMapRestaurantRVAdapter
import com.example.presentation.view.map.viewModel.VeganMapViewModel
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VeganMapFragment : BaseFragment<FragmentMainMapBinding>(R.layout.fragment_main_map) {
    private lateinit var mapView: MapView

    private val viewModel: VeganMapViewModel by viewModels()

    private lateinit var veganMapRestaurantRVAdapter: VeganMapRestaurantRVAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    private var currentLocation: LatLng? = null // 현재 위치를 저장할 변수
    private var kakaoMap: KakaoMap? = null // KakaoMap 객체를 저장할 변수

    private var isLocationUpdateRequested: Boolean = false
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult?.let { result ->
                for (location in result.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    logMessage("Location Update: Latitude = $latitude, Longitude = $longitude")
                    // 현재 위치 저장
                    currentLocation = LatLng.from(latitude, longitude)
                    // 지도 중심 이동
                    kakaoMap?.moveCamera(
                        CameraUpdateFactory.newCenterPosition(
                            LatLng.from(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude
                            )
                        )
                    )
                    CameraUpdateFactory.zoomTo(15)
                    // 주변 식당 정보 가져오기
                    viewModel.fetchNearRestaurantMap(0, latitude, longitude)
                    // 위치 업데이트 중지 (필요에 따라)
                    removeLocationUpdates()
                    break // 첫 번째 위치만 사용하고 루프 종료
                }
            }
        }
    }

    override fun init() {
        // 권한 체크
        checkAndRequestPermissions()

        // Floating button layer
        setFloatingLayer()
        // MapView
        initMap()

        // BottomSheet Recyclerview
        setRVAdapter()

        // 뒤로가기
        setBackUp()

        setUserInfo()
        // 제보하기 버튼
        reportRestaurant()

        findCurrentLocation()

        setBottomSheet()
    }

    private fun setUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nickName.collect { userName ->
                // 닉네임 바텀시트에 설정
                val headlineText = getString(R.string.map_bottom_sheet_title_nearby, userName)
                val headlineSpannable = SpannableString(headlineText)

                val userNameColor1 =
                    ContextCompat.getColor(requireContext(), R.color.color_primary)
                val userNameStartIndex1 = headlineText.indexOf(userName)
                val userNameEndIndex1 = userNameStartIndex1 + userName.length

                headlineSpannable.setSpan(
                    ForegroundColorSpan(userNameColor1),
                    userNameStartIndex1,
                    userNameEndIndex1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.includedBottomSheet.tvBottomSheetTitle.text = headlineSpannable
            }
        }
    }

    // Floating Layer 설정
    private fun setFloatingLayer() {
        val height = getBottomSheetDialogDefaultHeight(60)
        val layoutParams = binding.clCollapse.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.height = height
        binding.clCollapse.layoutParams = layoutParams
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.includedBottomSheet.clBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        logMessage("BottomSheetBehavior 접힘")
                    } // 접힘
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        logMessage("BottomSheetBehavior 펼쳐짐")
                    } // 펼쳐짐
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        logMessage("BottomSheetBehavior 숨겨짐")
                    }    // 숨겨짐
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        logMessage("BottomSheetBehavior 절반 펼쳐짐")
                    } // 절반 펼쳐짐
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        logMessage("BottomSheetBehavior 드래그하는 중")
                    }  // 드래그하는 중
                    BottomSheetBehavior.STATE_SETTLING -> {
                        logMessage("BottomSheetBehavior 안정화 단계")
                    }  // 안정화 단계
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                logMessage("slideOffset $slideOffset")
                // 비율에 따라 상태를 조정합니다.
            }

        })
    }

    private fun setRVAdapter() {
        veganMapRestaurantRVAdapter = VeganMapRestaurantRVAdapter()
        binding.includedBottomSheet.rvBottomSheetRestaurantList.adapter =
            veganMapRestaurantRVAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.restaurantList.collect { restaurantList ->
                logMessage("viewLifecycleOwner collect restaurantList $restaurantList")
                veganMapRestaurantRVAdapter.submitList(restaurantList)
            }
        }
        veganMapRestaurantRVAdapter.setOnItemClickListener(object :
            VeganMapRestaurantRVAdapter.OnItemClickListener {
            override fun onClick(data: VeganMapRestaurant) {
                logMessage("VeganMap onClick: $data")
                showToast("${data.name}")
                val action =
                    VeganMapFragmentDirections.actionVeganMapFragmentToRestaurantDetailFragment(
                        restaurantId = data.id,
                        latitude = data.latitude,
                        longitude = data.longitude,
                        imgUrl = data.thumbnail
                    )
                findNavController().navigate(action)
            }

        })
    }

    private fun reportRestaurant() {
        binding.fabMapReport.setOnClickListener {
            RestaurantReportDialog().show(childFragmentManager, "RestaurantReportDialog")
        }
    }

    private fun findCurrentLocation() {
        binding.fabCurrentLocation.setOnClickListener {
            showToast("현재 위치 찾기")
            getLocation()
        }
    }

    private fun initMap() {
        mapView = MapView(requireContext())
        binding.mapView.addView(mapView)
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                // KakaoMap 객체 저장
                kakaoMap = map

                // 현재 위치가 이미 확보되었다면 지도 중심 이동
                currentLocation?.let { location ->
                    kakaoMap?.moveCamera(
                        CameraUpdateFactory.newCenterPosition(
                            LatLng.from(
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                    CameraUpdateFactory.zoomTo(15)
                }
            }

            override fun getPosition(): LatLng {
                return LatLng.from(37.5665, 126.9780)
            }

            // Default Zoom Level 15
            override fun getZoomLevel(): Int {
                return 15
            }
        })
    }

    // BottomSheet Sizing | Height 70%
    private fun getBottomSheetDialogDefaultHeight(per: Int): Int {
        return getWindowHeight() * per / 100
        // 위 수치는 기기 높이 대비 설정한 퍼센트로 높이를 설정
    }

    private fun getWindowHeight(): Int {
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    // 위치 업데이트 요청 함수
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .apply {
                setMinUpdateDistanceMeters(10f) // 위치 업데이트 간의 최소 거리 (10미터)
                setGranularity(Granularity.GRANULARITY_FINE)
                setWaitForAccurateLocation(true)
            }.build()

        try {
            // 위치 업데이트 요청
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
            isLocationUpdateRequested = true
        } catch (e: SecurityException) {
            e.printStackTrace()
            // 위치 권한이 없는 경우 처리
            logMessage("Location permission not granted")
        }
    }

    // 위치 업데이트 요청 중지 함수
    private fun removeLocationUpdates() {
        if (isLocationUpdateRequested) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            isLocationUpdateRequested = false
        }
    }

    // 현재 위치를 가져오는 함수
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없는 경우 처리
            logMessage("Location permission Not Granted")
            return
        } else {
            logMessage("Location permission Granted")
        }

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    logMessage("Current Location: Latitude = $latitude, Longitude = $longitude")
                    // 현재 위치 저장
                    currentLocation = LatLng.from(latitude, longitude)
                    // 지도 중심 이동
                    kakaoMap?.moveCamera(
                        CameraUpdateFactory.newCenterPosition(
                            LatLng.from(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude
                            )
                        )
                    )
                    CameraUpdateFactory.zoomTo(15)
                    // 주변 식당 정보 가져오기
                    viewModel.fetchNearRestaurantMap(0, latitude, longitude)
                } ?: run {
                    // 마지막 위치를 가져올 수 없는 경우 위치 업데이트 요청
                    requestLocationUpdates()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                // 위치 정보를 가져오는 데 실패한 경우 처리
                logMessage("Failed to get location")
            }
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
                    showPermissionRationaleDialog()
                } else {
                    logMessage(
                        "shouldShowRequestPermissionRationale = false"
                    )
                    locationPermissionLauncher.launch(permissions)
                }

            }
        }
    }

    // 권한 재요청 다이얼로그
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

    // 권한 허용 안함 다이얼로그
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
        const val SEARCH_DEFAULT = 0
        const val SEARCH_RESULT = 1
        private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    private fun setBackUp() {
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_veganMapFragment_to_veganMapSearchFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
        removeLocationUpdates() // 위치 업데이트 중지
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeLocationUpdates() // 위치 업데이트 중지
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            val isFineLocation = isGranted[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val isCoarseLocation = isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            when {
                isFineLocation && isCoarseLocation -> {
                    // 정확한 위치 권한 승인
                    logMessage("locationPermissionLauncher Fine Location, Coarse Location Granted 정확한 위치 권한 승인")
                    getLocation()
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

}
