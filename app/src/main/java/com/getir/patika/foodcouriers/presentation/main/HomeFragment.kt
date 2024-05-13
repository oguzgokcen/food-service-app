package com.getir.patika.foodcouriers.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.bumptech.glide.Glide
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.databinding.FragmentHomeBinding
import com.getir.patika.foodcouriers.databinding.ItemCategoryBinding
import com.getir.patika.foodcouriers.databinding.ItemCategoryViewBinding
import com.getir.patika.foodcouriers.databinding.ItemFoodsBinding
import com.getir.patika.foodcouriers.databinding.ItemFoodsViewBinding
import com.getir.patika.foodcouriers.databinding.ItemSearchBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Food
import com.getir.patika.foodcouriers.presentation.HomeViewModel
import com.getir.patika.foodcouriers.presentation.adapter.SingleRecylerAdapter
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    private val searchAdapter = SingleRecylerAdapter<ItemSearchBinding, String>(
        { inflater, _, _ ->
            ItemSearchBinding.inflate(
                inflater,
                binding.rvHome,
                false
            )
        },
        { binding, item ->

        }
    )


    private val categoryItemAdapter = SingleRecylerAdapter<ItemCategoryViewBinding, String>(
        { inflater, _, _ ->
            ItemCategoryViewBinding.inflate(
                inflater,
                binding.rvHome,
                false
            )
        },
        { binding, item ->


            binding.apply {
                binding.chip.text = item

                chip.setOnClickListener {

                    if (chip.isChecked) {
                        chip.chipStrokeWidth = 0f
                        Toast.makeText(it.context, "Selected", Toast.LENGTH_SHORT).show()
                    } else {
                        chip.chipStrokeWidth = 1f
                        Toast.makeText(it.context, "Not Selected", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.root.setOnClickListener {
                binding.apply {

                }
            }
        }
    )

    private fun getFoodByCategories() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.foodCategories.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { viewState ->
                        when (viewState) {
                            is ViewState.Success -> {
                                val response = viewState.result as BaseResponse.Success
                                foodsByCategoryItem.data = response.data
                                Log.d("HomeFragment", response.data.toString())
                            }

                            is ViewState.Error -> {
                                val responseError = viewState.error
                                Log.v("MyViewState", responseError)
                            }

                            is ViewState.Loading -> {
                                Log.v("MyViewState", "ViewState.Loading")
                            }
                        }
                    }
            }
        }
    }

    private val foodsByCategoryItem = SingleRecylerAdapter<ItemFoodsViewBinding, Food>(
        { inflater, _, _ ->
            ItemFoodsViewBinding.inflate(
                inflater,
                binding.rvHome,
                false
            )
        },
        { binding, item ->
            binding.apply {
                tvFoodName.text = item.name
                tvFoodDescription.text = item.description
                tvFoodPrice.text = item.price
                tvRating.text = item.rating.toString()
                Glide.with(requireContext()).load(item.imageUrl).into(ivFood)
            }
        }
    )
    private val foodsByCategory = SingleRecylerAdapter<ItemFoodsBinding, String>(
        { inflater, _, _ ->
            ItemFoodsBinding.inflate(
                inflater,
                binding.rvHome,
                false
            )
        },
        { binding, item ->
            binding.apply {
                binding.rvFoodsByCategory.adapter = foodsByCategoryItem

            }
        }
    )

    private val categoryAdapter = SingleRecylerAdapter<ItemCategoryBinding, String>(
        { inflater, _, _ ->
            ItemCategoryBinding.inflate(
                inflater,
                binding.rvHome,
                false
            )
        },
        { binding, item ->
            binding.rvCategory.adapter = categoryItemAdapter
        }
    )

    private val concatAdapter = ConcatAdapter(
        foodsByCategory,
        categoryAdapter
//        categoryItemAdapter,
//        foodsByCategoryItem,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListener()


        viewModel.getFoodCategoriesList()
        getFoodByCategories()

        binding.apply {

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.foodCategoriesList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { viewState ->
                        when (viewState) {
                            is ViewState.Success -> {
                                val response = viewState.result as BaseResponse.Success
                                categoryItemAdapter.data = response.data
                            }

                            is ViewState.Error -> {
                                val responseError = viewState.error
                                Log.v("MyViewState", responseError)
                            }

                            is ViewState.Loading -> {
                                Log.v("MyViewState", "ViewState.Loading")

                            }
                        }
                    }
            }
        }
    }

    private fun initListener() = with(binding) {
        rvHome.adapter = concatAdapter
    }

    private fun initData() {
        categoryAdapter.data = listOf("categoryAdapter")
        foodsByCategory.data = listOf("foodsByCategory")
    }

    companion object {

    }
}