package com.getir.patika.foodcouriers.presentation.orderdetails

import android.widget.Toast
import com.getir.patika.chatapp.data.model.FakeOrderItems
import com.getir.patika.foodcouriers.databinding.FragmentOrderDetailsBinding

class OrderFragment : BaseFragment<FragmentOrderDetailsBinding>() {
    override fun FragmentOrderDetailsBinding.initializeViews() {
        val adapter = OrderDetailsAdapter { event ->
            when (event) {
                is OrderDetailsEvent.OnPlusClick -> {
                    Toast.makeText(
                        requireContext(),
                        "Plus clicked: ${event.itemId}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is OrderDetailsEvent.OnMinusClick -> {
                    Toast.makeText(
                        requireContext(),
                        "Minus clicked: ${event.itemId}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        adapter.saveData(FakeOrderItems)
        rvOrderItems.adapter = adapter
        showLoading()
    }
}
