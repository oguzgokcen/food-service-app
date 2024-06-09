package com.getir.patika.foodcouriers.domain.model.PaymentModels

data class Payment(
    val orderId:Int,
    val cardNumber: Long,
    val cardExpiryMonth: Int,
    val cardExpiryYear: Int,
    val cvv: Int
)
