package com.example.presentation.view.restaurant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.map.RestaurantDetail
import com.example.domain.useCase.map.restaurant.GetRestaurantDetailUseCase
import com.example.domain.useCase.review.RestaurantReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
    private val restaurantReviewUseCase: RestaurantReviewUseCase
) : ViewModel() {

    private val _restaurantDetail = MutableStateFlow(RestaurantDetail())
    val restaurantDetail: StateFlow<RestaurantDetail> get() = _restaurantDetail

    fun getRestaurantDetail(restaurantId: Long, latitude: String, longitude: String) {

        viewModelScope.launch {
            getRestaurantDetailUseCase.invoke(restaurantId, latitude, longitude)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.d(e, "getRestaurantDetail Exception")
                }
                .collect { restaurantDetail ->
                    Timber.d("getRestaurantDetail data $restaurantDetail")
                    _restaurantDetail.value = restaurantDetail
                }
        }
    }
}