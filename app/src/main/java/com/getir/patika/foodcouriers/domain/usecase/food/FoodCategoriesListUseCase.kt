package com.getir.patika.foodcouriers.domain.usecase.food

import com.getir.patika.foodcouriers.common.domain.NoParaMeterUseCase
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FoodCategoriesListUseCase @Inject constructor(
    private val foodRepository: FoodRepository
): NoParaMeterUseCase<Flow<BaseResponse<List<String>>>> {

    override fun execute(): Flow<BaseResponse<List<String>>> = foodRepository.getCategoriesList()
}