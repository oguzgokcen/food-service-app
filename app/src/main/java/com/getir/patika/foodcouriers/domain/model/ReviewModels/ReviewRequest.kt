package com.getir.patika.foodcouriers.domain.model.ReviewModels

data class ReviewRequest(val orderId:Int,val reviewBody:String?,val star:Int)
