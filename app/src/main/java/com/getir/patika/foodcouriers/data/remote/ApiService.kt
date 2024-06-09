package com.getir.patika.foodcouriers.data.remote

import com.getir.patika.foodcouriers.domain.model.CreateAccountResponse
import com.getir.patika.foodcouriers.domain.model.Food.Food
import com.getir.patika.foodcouriers.domain.model.FoodCategory
import com.getir.patika.foodcouriers.domain.model.Location
import com.getir.patika.foodcouriers.domain.model.Login
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderRequest
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderResponse
import com.getir.patika.foodcouriers.domain.model.Orders
import com.getir.patika.foodcouriers.domain.model.PaymentModels.Payment
import com.getir.patika.foodcouriers.domain.model.PaymentModels.PaymentResponse
import com.getir.patika.foodcouriers.domain.model.Profile
import com.getir.patika.foodcouriers.domain.model.Register
import com.getir.patika.foodcouriers.domain.model.ReviewModels.ReviewRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/register")
    fun setRegister(@Body register: Register): Call<CreateAccountResponse>

    @POST("auth/login")
    fun setLogin(@Body login: Login): Call<String>

    @GET("api/v1/products/{restourantId}")
    fun getAllFoods(@Header("Authorization") token:String,@Path("restourantId") restourantId:Int): Call<List<Food>>

    @GET("api/v1/restaurants/categories/{restourantId}")
    fun getFoodCategoriesList(@Header("Authorization") token:String,@Path("restourantId") restourantId:Int): Call<List<FoodCategory>>

    @POST("api/v1/reviews")
    fun postReview(@Header("Authorization") token:String,@Body reviewRequest: ReviewRequest): Call<Void>

    @POST("api/v1/orders")
    fun setOrders(@Header("Authorization") token:String,@Body orderRequest: OrderRequest): Call<OrderResponse>

    @POST("api/v1/payments")
    fun makePayment(@Header("Authorization") token:String,@Body payment: Payment):  Call<PaymentResponse>
    @GET("profile/{userId}")
    fun getProfile(@Path("userId") userId: String): Call<Profile>

    @POST("profile/{userId}/location")
    fun setLocation(@Path("userId") userId: String,@Body location: Location): Call<Boolean>

    @GET("foods")
    fun getSearchFoods(@Query("search") search: String): Call<List<Food>>

    @GET("foods/categories")
    fun getCategoriesFoods(@Query("type") type: String): Call<List<Food>>

    @GET("orders/{userId}")
    fun getActiveOrders(@Path("userId") userId: String): Call<Orders>

    @POST("orders/items/{itemId}")
    fun setUpdateOrder(@Path("itemId") itemId: Int,@Body quantity: Int): Call<Any>

    @DELETE("orders/{userId}/current")
    fun deleteCurrentOrders(@Path("userId") userId: String): Call<Any>




}