package com.getir.patika.foodcouriers.presentation.food

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.databinding.FragmentDetailMenuBinding
import com.getir.patika.foodcouriers.domain.model.Food.Food
import com.getir.patika.foodcouriers.presentation.HomeViewModel
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMenuFragment : Fragment(R.layout.fragment_detail_menu) {
    private val binding by viewBinding(FragmentDetailMenuBinding::bind)
    private val viewModel: HomeViewModel by activityViewModels()

    private val args: DetailMenuFragmentArgs by navArgs()
    lateinit var food: Food
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        food=args.food
        bindItem(food)
    }

    fun bindItem(item: Food) {
        binding.apply {
            tvFoodTitle.text = item.productName
            tvRating.text = String.format("%.2f", item.productRating)
            tvDesc.text = item.productDescription
            tvOrder.text= item.orderCount.toString()
            Glide.with(requireContext())
                .load(item.productImage)
                .into(ivFood)
            btAddCard.setOnClickListener {
                viewModel.addFood(item)
                Toast.makeText(requireContext(), "Added to card", Toast.LENGTH_SHORT).show()
            }
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    companion object {

    }
}