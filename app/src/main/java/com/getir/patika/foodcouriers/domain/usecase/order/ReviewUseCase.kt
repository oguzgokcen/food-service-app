package com.getir.patika.foodcouriers.domain.usecase.order

import com.getir.patika.foodcouriers.common.domain.SingleParaMeterUseCase
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.ReviewModels.ReviewRequest
import com.getir.patika.foodcouriers.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewUseCase @Inject constructor(
    private val orderRepository: OrderRepository
): SingleParaMeterUseCase<ReviewRequest, Flow<BaseResponse<Void>>> {

    override fun execute(param: ReviewRequest): Flow<BaseResponse<Void>> = orderRepository.postReview(param)


}