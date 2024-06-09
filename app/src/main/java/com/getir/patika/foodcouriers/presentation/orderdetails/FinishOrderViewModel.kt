package com.getir.patika.foodcouriers.presentation.orderdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.ReviewModels.ReviewRequest
import com.getir.patika.foodcouriers.domain.usecase.food.FoodCategoriesListUseCase
import com.getir.patika.foodcouriers.domain.usecase.food.FoodsUseCase
import com.getir.patika.foodcouriers.domain.usecase.order.OrderUseCase
import com.getir.patika.foodcouriers.domain.usecase.order.ReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FinishOrderViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {

    private val _postReviewStateFlow: MutableStateFlow<ViewState<BaseResponse<Void>>> =
        MutableStateFlow(ViewState.Loading)
    val PostReviewStateFlow = _postReviewStateFlow.asStateFlow()

    fun postReview(reviewRequest: ReviewRequest) {
        reviewUseCase.execute(reviewRequest).map {
            when (val responseData: BaseResponse<Void> = it) {
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }

                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach {data ->
            _postReviewStateFlow.emit(data)
        }.catch {
            _postReviewStateFlow.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)
    }
}