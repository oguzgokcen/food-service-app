package com.getir.patika.foodcouriers.presentation.orderdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.getir.patika.chatapp.data.model.OrderItem
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.databinding.FragmentOrderDetailsBinding
import com.getir.patika.foodcouriers.databinding.ItemFoodOrderBinding
import com.getir.patika.foodcouriers.databinding.ItemFoodsViewBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Food.Food
import com.getir.patika.foodcouriers.presentation.HomeViewModel
import com.getir.patika.foodcouriers.presentation.adapter.SingleRecylerAdapter
import com.getir.patika.foodcouriers.presentation.main.HomeFragmentDirections
import com.wada811.viewbindingktx.viewBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class OrderFragment : Fragment(R.layout.fragment_order_details){
    private val binding by viewBinding(FragmentOrderDetailsBinding::bind)
    private val viewModel: HomeViewModel by activityViewModels()

    private val foodsByCategoryItem = SingleRecylerAdapter<ItemFoodOrderBinding, OrderItem>(
        { inflater, _, _ ->
            ItemFoodOrderBinding.inflate(
                inflater,
                binding.rvOrderItems,
                false
            )
        },
        { binding, item ->
            binding.apply {
                tvFoodPrice.text ="$"+ item.price.toString()
                tvCount.text = item.quantity.toString()
                tvFoodTitle.text = item.name
                Glide.with(binding.root.context).load(item.imageUrl).into(ivFood)
//                tvFoodSubHeader.text = orderItem.place
                btnPlus.setOnClickListener {
                    val count = viewModel.addOrderItem(item)
                    tvCount.text = count.toString()

                }
                btnMinus.setOnClickListener {
                    val count = viewModel.decreaseOrderItem(item)
                    tvCount.text = count.toString()
                }
            }

        }
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = foodsByCategoryItem
        adapter.data = viewModel.orderList
        with(binding) {
            rvOrderItems.adapter = adapter
            btnPlaceOrder.setOnClickListener {
         //       startActivity(FinishOrderFragment.callIntent(requireActivity()))
                binding.loadingView.visibility = View.VISIBLE
                viewModel.placeOrder()
            }
        }
        viewModel.getCardTotal().observe(viewLifecycleOwner){
            binding.tvSubTotal.text = "$" + it
            binding.tvTotal.text = "$" + (it+10)
        }

        initObserver()
    }

    fun initObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.placeOrderStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            binding.apply {
                                loadingView.visibility = View.GONE
//                                viewModel.emptyCard()
//                                val intent = FinishOrderFragment.callIntent(requireActivity())
//                                intent.putExtra("orderId", response.data.id)
                                val direction = OrderFragmentDirections.actionOrderFragmentToPaymentFragment(response.data.id)
                                findNavController().navigate(direction)
                                Toast.makeText(context, "Order placed successfully", Toast.LENGTH_SHORT).show()
                            }
                        }

                        is ViewState.Error -> {
                            val responseError = viewState.error
                            Log.v("foodCategoriesList", "ViewState.Error")
                            binding.loadingView.visibility = View.GONE
                            //categoryItemAdapter.data = listOf("Burger", "Pizza", "Pasta", "Salad", "Dessert")
                        }

                        is ViewState.Loading -> {
                            Log.v("MyViewState", "ViewState.Loading")

                        }
                    }
                }
        }
    }
}
