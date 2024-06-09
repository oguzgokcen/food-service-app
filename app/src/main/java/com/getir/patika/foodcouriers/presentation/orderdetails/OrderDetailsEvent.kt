package com.getir.patika.foodcouriers.presentation.orderdetails

import com.getir.patika.chatapp.data.model.OrderItem
import com.getir.patika.foodcouriers.domain.model.Order

sealed class OrderDetailsEvent {
    data class OnPlusClick(val item :OrderItem) : OrderDetailsEvent()
    data class OnMinusClick(val item:OrderItem) : OrderDetailsEvent()
}
