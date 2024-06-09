package com.getir.patika.foodcouriers.presentation.food

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.databinding.FragmentSearchBinding
import com.getir.patika.foodcouriers.databinding.ItemSearchMealBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Food.Food
import com.getir.patika.foodcouriers.presentation.adapter.SingleRecylerAdapter
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchMealViewModel by viewModels()
    private var mTextWatcher: TextWatcher? = null

    private val searchAdapter = SingleRecylerAdapter<ItemSearchMealBinding, Food>(
        { inflater, _, _ ->
            ItemSearchMealBinding.inflate(
                inflater,
                binding.rvSearchMeal,
                false
            )
        },
        { binding, item ->
            with(binding) {
                searchMealName.text = item.productName
                context?.let {
                    Glide.with(it)
                        .load(item.productImage)
                        .into(binding.imgSearchMeal)
                }
                searchMealDescription.text = item.productShortDescription
                mealPrice.text = "$" + item.productPrice
            }
        }
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
    }

    private fun initObserver() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiStateFood.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            searchAdapter.data = response.data
                            binding.rvSearchMeal.layoutManager =
                                LinearLayoutManager(context)
                            binding.rvSearchMeal.adapter = searchAdapter
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


    private fun initListener() = textChanger()


    private fun textChanger() {
        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length >= 2) {
                    viewModel.searchFood(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
        binding.searchBar.addTextChangedListener(mTextWatcher)
    }


    companion object {

    }
}