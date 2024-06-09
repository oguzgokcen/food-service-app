package com.getir.patika.foodcouriers.domain.usecase.order
import com.getir.patika.foodcouriers.common.domain.SingleParaMeterUseCase
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.PaymentModels.Payment
import com.getir.patika.foodcouriers.domain.model.PaymentModels.PaymentResponse
import com.getir.patika.foodcouriers.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MakePaymentUseCase @Inject constructor(
    private val orderRepository: OrderRepository
): SingleParaMeterUseCase<Payment, Flow<BaseResponse<PaymentResponse>>>  {
    override fun execute(param: Payment): Flow<BaseResponse<PaymentResponse>> =
        orderRepository.makePayment(param)

}