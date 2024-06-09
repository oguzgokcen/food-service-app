package com.getir.patika.foodcouriers.domain.model.OrderModels

data class OrderRequest(val restaurantId: String,val productList: List<ProductListElement>,val totalPrice:Int)
