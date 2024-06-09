package com.getir.patika.foodcouriers.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.databinding.FragmentHomeBinding
import com.getir.patika.foodcouriers.databinding.ItemCategoryViewBinding
import com.getir.patika.foodcouriers.databinding.ItemFoodsBinding
import com.getir.patika.foodcouriers.databinding.ItemFoodsViewBinding
import com.getir.patika.foodcouriers.databinding.ItemSearchBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Food.Food
import com.getir.patika.foodcouriers.domain.model.FoodCategory
import com.getir.patika.foodcouriers.presentation.HomeViewModel
import com.getir.patika.foodcouriers.presentation.adapter.SingleRecylerAdapter
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by activityViewModels()
    private val foodList:List<Food> = listOf(  //fakedata to be deleted
    )

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


    private val categoryItemAdapter = SingleRecylerAdapter<ItemCategoryViewBinding, FoodCategory>(
        { inflater, _, _ ->
            ItemCategoryViewBinding.inflate(
                inflater,
                binding.rvHome,
                false
            )
        },
        { binding, item ->
            binding.apply {
                chip.text = item.name
                when(item.name.uppercase()){
                    "HAMBURGER"-> chip.chipIcon = resources.getDrawable(R.drawable.hamburger)
                    "PIZZA"-> chip.chipIcon = resources.getDrawable(R.drawable.pizza)
                    "DONER"-> chip.chipIcon = resources.getDrawable(R.drawable.doner)
                    "FISH"-> chip.chipIcon = resources.getDrawable(R.drawable.fish)
                    "SALATA"-> chip.chipIcon = resources.getDrawable(R.drawable.salad)
                    "UZAK DOGU"-> chip.chipIcon = resources.getDrawable(R.drawable.ramen)
                }

                chip.setOnClickListener {

                    if (chip.isChecked) {
                        chip.chipStrokeWidth = 0f
                        foodsByCategoryItem.data= foodsByCategoryItem.data.filter { it.category== item}
                        Toast.makeText(it.context, "Selected", Toast.LENGTH_SHORT).show()
                    } else {
                        chip.chipStrokeWidth = 1f
                        foodsByCategoryItem.data = viewModel.foodItems
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
                tvFoodName.text = item.productName
                tvFoodDescription.text = item.productShortDescription
                tvFoodPrice.text = "$"+item.productPrice
                tvRating.text = item.productRating.toString()
                val glideUrl = GlideUrl(
                    item.productImage, LazyHeaders.Builder()
                        .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 OPR/110.0.0.0"
                        )
                        .build()
                )
                Glide.with(requireContext()).load(glideUrl).into(ivFood)
                ivAddToCart.setOnClickListener {
                    viewModel.addFood(item)
                    Toast.makeText(it.context, "Added to cart", Toast.LENGTH_SHORT).show()
                }
                cvFoodItem.setOnClickListener {
                    val direction = HomeFragmentDirections.actionHomeFragmentToDetailMenuFragment(item)
                    findNavController().navigate(direction)
                }

            }

        }
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingView.visibility = View.VISIBLE
//        viewModel.getFoodCategoriesList()
//        viewModel.getFoods()
        initObserver()
        initListener()
        binding.apply {
            rvHome.adapter=categoryItemAdapter
            rvGrid.layoutManager = GridLayoutManager(requireContext(), 2)
            rvGrid.adapter = foodsByCategoryItem
        }
    }


    private fun initListener() = with(binding) {
        //rvHome.adapter = concatAdapter
        searchBar.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(direction)
        }
    }
    private fun initObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.foodCategories.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            categoryItemAdapter.data = response.data
                        }

                        is ViewState.Error -> {
                            val responseError = viewState.error
                            Log.v("foodCategoriesList", "ViewState.Error")
                            //categoryItemAdapter.data = listOf("Burger", "Pizza", "Pasta", "Salad", "Dessert")
                        }

                        is ViewState.Loading -> {
                            Log.v("MyViewState", "ViewState.Loading")

                        }
                    }
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.foodStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            foodsByCategoryItem.data = response.data
                            viewModel.foodItems = response.data.toMutableList()
                            binding.loadingView.visibility = View.GONE
                            //Log.d("HomeFragment", response.data.toString())
                        }

                        is ViewState.Error -> {
                            val responseError = viewState.error
                            Log.v("HomeFragment", "Error " + responseError)
                            foodsByCategoryItem.data = foodList

                        }

                        is ViewState.Loading -> {
                            Log.v("MyViewState", "ViewState.Loading")
                        }
                    }
                }
        }
    }

    companion object {

    }
}