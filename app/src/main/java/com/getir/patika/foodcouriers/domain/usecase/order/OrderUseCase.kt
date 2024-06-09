package com.getir.patika.foodcouriers.domain.usecase.order

import com.getir.patika.foodcouriers.common.domain.MultiParaMeterUseCase
import com.getir.patika.foodcouriers.common.domain.SingleParaMeterUseCase
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderRequest
import com.getir.patika.foodcouriers.domain.model.OrderModels.OrderResponse
import com.getir.patika.foodcouriers.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
): SingleParaMeterUseCase<OrderRequest, Flow<BaseResponse<OrderResponse>>> {

    override fun execute(param: OrderRequest): Flow<BaseResponse<OrderResponse>>  =
        orderRepository.setOrders(param)

}