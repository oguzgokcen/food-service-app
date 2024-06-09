package com.getir.patika.foodcouriers.domain.model.Food

import com.getir.patika.foodcouriers.domain.model.FoodCategory
import java.io.Serializable

data class Food(
    val id:Int,
    val category: FoodCategory,
    val productDescription: String,
    val productShortDescription: String,
    val productImage: String,
    val productName: String,
    val orderCount: Int,
    val productPrice: Int,
    val tags: List<Tags>,
    val productRating:Double
):Serializable