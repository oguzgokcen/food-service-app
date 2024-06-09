package com.getir.patika.foodcouriers.domain.repository

import com.getir.patika.foodcouriers.data.local.DataStoreManager
import com.getir.patika.foodcouriers.data.remote.ApiService
import com.getir.patika.foodcouriers.data.remote.CallBack
import com.getir.patika.foodcouriers.di.IoDispatcher
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderRequest
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderResponse
import com.getir.patika.foodcouriers.domain.model.Orders
import com.getir.patika.foodcouriers.domain.model.PaymentModels.Payment
import com.getir.patika.foodcouriers.domain.model.PaymentModels.PaymentResponse
import com.getir.patika.foodcouriers.domain.model.ReviewModels.ReviewRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    var dataStoreManager: DataStoreManager
) {

    fun setOrders(orderRequest: OrderRequest): Flow<BaseResponse<OrderResponse>>  = callbackFlow {
        dataStoreManager.token.collect{token->
            apiService.setOrders(token!!,orderRequest).enqueue(CallBack(this.channel))
        }
        awaitClose{ close()}
    }.flowOn(ioDispatcher)

    fun makePayment(payment: Payment): Flow<BaseResponse<PaymentResponse>> = callbackFlow {
        dataStoreManager.token.collect { token ->
            apiService.makePayment(token!!,payment).enqueue(CallBack(this.channel))
        }
        awaitClose{ close()}
    }.flowOn(ioDispatcher)

    fun getActiveOrders(userId: String): Flow<BaseResponse<Orders>> = callbackFlow {
        apiService.getActiveOrders(userId).enqueue(CallBack(this.channel))
        awaitClose{ close()}
    }.flowOn(ioDispatcher)

    fun setUpdateOrder(itemId: Int,quantity: Int): Flow<BaseResponse<Any>> = callbackFlow {
        apiService.setUpdateOrder(itemId,quantity).enqueue(CallBack(this.channel))
        awaitClose{ close()}
    }.flowOn(ioDispatcher)

    fun deleteCurrentOrders(userId: String): Flow<BaseResponse<Any>> = callbackFlow {
        apiService.deleteCurrentOrders(userId).enqueue(CallBack(this.channel))
        awaitClose{ close()}
    }.flowOn(ioDispatcher)

    fun postReview(reviewRequest: ReviewRequest): Flow<BaseResponse<Void>> = callbackFlow {
        dataStoreManager.token.collect { token ->
            apiService.postReview(token!!,reviewRequest).enqueue(CallBack(this.channel))
        }
        awaitClose{ close()}
    }.flowOn(ioDispatcher)



}