package com.getir.patika.foodcouriers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Food
import com.getir.patika.foodcouriers.domain.usecase.food.CategoriesFoodsUseCase
import com.getir.patika.foodcouriers.domain.usecase.food.FoodCategoriesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodCategoriesListUseCase: FoodCategoriesListUseCase,
    private val categoriesFoodsUseCase: CategoriesFoodsUseCase
) : ViewModel() {

    private val _foodCategoriesList: MutableStateFlow<ViewState<BaseResponse<List<String>>>> =
        MutableStateFlow(ViewState.Loading)
    val foodCategoriesList = _foodCategoriesList.asStateFlow()

    private val _foodCategories: MutableStateFlow<ViewState<BaseResponse<List<Food>>>> =
        MutableStateFlow(ViewState.Loading)
    val foodCategories = _foodCategories.asStateFlow()

    init {
        getFoodCategories("Burger")
        getFoodCategoriesList()
    }

    fun getFoodCategoriesList() {
        foodCategoriesListUseCase.execute().map {
            when (val responseData: BaseResponse<List<String>> = it) {
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }

                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach {data ->
            _foodCategoriesList.emit(data)
        }.catch {
            _foodCategoriesList.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)
    }

    fun getFoodCategories(type: String) {
        categoriesFoodsUseCase.execute(type).map {
            when (val responseData: BaseResponse<List<Food>> = it) {
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }

                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach {data ->
            _foodCategories.emit(data)
        }.catch {
            _foodCategories.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)
    }
}