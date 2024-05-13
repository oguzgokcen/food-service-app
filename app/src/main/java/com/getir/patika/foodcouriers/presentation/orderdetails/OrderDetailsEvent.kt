package com.getir.patika.foodcouriers.presentation.orderdetails

sealed class OrderDetailsEvent {
    data class OnPlusClick(val itemId: Int) : OrderDetailsEvent()
    data class OnMinusClick(val itemId: Int) : OrderDetailsEvent()
}
