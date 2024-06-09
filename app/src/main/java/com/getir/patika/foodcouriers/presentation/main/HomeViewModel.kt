package com.getir.patika.foodcouriers.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getir.patika.chatapp.data.model.OrderItem
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Food.Food
import com.getir.patika.foodcouriers.domain.model.FoodCategory
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderRequest
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderResponse
import com.getir.patika.foodcouriers.domain.model.OrderModels.ProductListElement
import com.getir.patika.foodcouriers.domain.model.PaymentModels.Payment
import com.getir.patika.foodcouriers.domain.model.PaymentModels.PaymentResponse
import com.getir.patika.foodcouriers.domain.usecase.food.FoodCategoriesListUseCase
import com.getir.patika.foodcouriers.domain.usecase.food.FoodsUseCase
import com.getir.patika.foodcouriers.domain.usecase.order.MakePaymentUseCase
import com.getir.patika.foodcouriers.domain.usecase.order.OrderUseCase
import com.getir.patika.foodcouriers.domain.usecase.order.ReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodCategoriesListUseCase: FoodCategoriesListUseCase,
    private val foodsUseCase: FoodsUseCase,
    private val orderUseCase:OrderUseCase,
    private val reviewUseCase: ReviewUseCase,
    private val makePaymentUseCase: MakePaymentUseCase
) : ViewModel() {

    private val _foodCategories: MutableStateFlow<ViewState<BaseResponse<List<FoodCategory>>>> =
        MutableStateFlow(ViewState.Loading)
    val foodCategories = _foodCategories.asStateFlow()

    private val _foodStateFlow: MutableStateFlow<ViewState<BaseResponse<List<Food>>>> =
        MutableStateFlow(ViewState.Loading)
    val foodStateFlow = _foodStateFlow.asStateFlow()

    private val _placeOrderStateFlow: MutableSharedFlow<ViewState<BaseResponse<OrderResponse>>> =
        MutableSharedFlow()
    val placeOrderStateFlow = _placeOrderStateFlow.asSharedFlow()

    private val _makePaymentStateFlow: MutableSharedFlow<ViewState<BaseResponse<PaymentResponse>>> =
        MutableSharedFlow()
    val MakePaymentStateFlow = _makePaymentStateFlow.asSharedFlow()

    val restourantId = 1
    var orderList: MutableList<OrderItem>
    private var cardTotal = MutableLiveData(0)
    var foodItems:MutableList<Food> = mutableListOf()
    fun getCardTotal():MutableLiveData<Int> = cardTotal
    init {
        getFoods()
        getFoodCategoriesList()
        orderList = mutableListOf()
    }
    fun addFood(item: Food):Int {
        return if (item.id in orderList.map { it.id }) {
            val cardItem = orderList.find { it.id == item.id }
            cardItem?.increaseQuantity()
            updateCardTotal()
            cardItem?.quantity ?: 0
        } else {
            orderList.add(OrderItem(item.id, item.productName, item.productPrice, item.productImage))
            updateCardTotal()
            1
        }
    }
    fun addOrderItem(orderItem: OrderItem):Int{
        return if (orderItem.id in orderList.map { it.id }) {
            val cardItem = orderList.find { it.id == orderItem.id }
            cardItem?.increaseQuantity()
            updateCardTotal()
            cardItem?.quantity ?: 0
        } else {
            orderList.add(orderItem)
            updateCardTotal()
            1
        }
    }
    fun decreaseOrderItem(orderItem: OrderItem):Int{
        if (orderItem.id in orderList.map { it.id }) {
            val cardItem = orderList.find { it.id == orderItem.id }
            cardItem?.decreaseQuantity()
            if (cardItem?.quantity == 0) {
                orderList.remove(cardItem)
            }
            updateCardTotal()
            return cardItem?.quantity ?: 0
        }else{
            return -1
        }
    }

    fun emptyCard(){
        orderList.clear()
        updateCardTotal()
    }
    fun updateCardTotal() {
        if(orderList.isEmpty()){
            cardTotal.value = 0
            return
        }
        cardTotal.value = orderList.sumOf { it.price * it.quantity}
    }
    fun getFoodCategoriesList() {
        foodCategoriesListUseCase.execute(restourantId).map {
            when (val responseData: BaseResponse<List<FoodCategory>> = it) {
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

    fun getFoods() {
        foodsUseCase.execute(restourantId).map {
            when (val responseData: BaseResponse<List<Food>> = it) {
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }

                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach {data ->
            _foodStateFlow.emit(data)
        }.catch {
            _foodStateFlow.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)
    }

    fun placeOrder(){
        if(orderList.isEmpty()){
            throw Exception("Order list is empty")
        }
        val productList:List<ProductListElement> = orderList.map {
            ProductListElement(it.id,it.quantity)
        }
        val orderRequest = OrderRequest(restourantId.toString(),productList,cardTotal.value!!+10)
        orderUseCase.execute(orderRequest).map {
            when (val responseData: BaseResponse<OrderResponse> = it) {
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }

                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach {data ->
            _placeOrderStateFlow.emit(data)
        }.catch {
            _placeOrderStateFlow.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)
    }

    fun doPayment(payment: Payment){
        makePaymentUseCase.execute(payment).map {
            when(val responseData: BaseResponse<PaymentResponse> = it){
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }
                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach {data ->
            _makePaymentStateFlow.emit(data)
        }.catch {
            _makePaymentStateFlow.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)
    }
}