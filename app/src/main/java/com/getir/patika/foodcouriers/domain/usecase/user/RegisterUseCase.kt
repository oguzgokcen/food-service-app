package com.getir.patika.foodcouriers.domain.usecase.user

import com.getir.patika.foodcouriers.common.domain.SingleParaMeterUseCase
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.CreateAccountResponse
import com.getir.patika.foodcouriers.domain.model.Register
import com.getir.patika.foodcouriers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) : SingleParaMeterUseCase<Register, Flow<BaseResponse<CreateAccountResponse>>> {

    override fun execute(param: Register): Flow<BaseResponse<CreateAccountResponse>> = userRepository.setRegister(param)

}