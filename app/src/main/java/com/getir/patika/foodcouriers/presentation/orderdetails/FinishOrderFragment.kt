package com.getir.patika.foodcouriers.presentation.orderdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.getir.patika.foodcouriers.MainActivity
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.databinding.FragmentFinishOrderBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.ReviewModels.ReviewRequest
import com.getir.patika.foodcouriers.presentation.HomeViewModel
import com.getir.patika.foodcouriers.presentation.main.HomeFragment
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FinishOrderFragment : AppCompatActivity() {
    private val binding by viewBinding(FragmentFinishOrderBinding::bind)
    private val viewModel: FinishOrderViewModel by viewModels()
    var orderId:Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_finish_order)
        orderId = intent.getIntExtra("orderId",0)
        initListener()
        initObserver()
    }
    fun initListener(){
        with(binding){
            btSubmit.setOnClickListener {
                val rating= binding.customRatingBar.rating
                val comment= binding.etReview.text.toString()
                val reviewRequest= ReviewRequest(orderId,comment,rating)
                loadingView.visibility = View.VISIBLE
                viewModel.postReview(reviewRequest)
            }
            btSkip.setOnClickListener {
                startActivity(MainActivity.callIntent(applicationContext))
                finish()
            }
        }
    }
    fun initObserver(){
        lifecycleScope.launch {
            viewModel.PostReviewStateFlow.flowWithLifecycle(lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            binding.apply {
                                loadingView.visibility = View.GONE
                                Toast.makeText(applicationContext, "Review Posted Successfully", Toast.LENGTH_SHORT).show()
                                startActivity(MainActivity.callIntent(applicationContext))
                                finish()
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
    companion object {
        fun callIntent(context: Context): Intent {
            return Intent(context, FinishOrderFragment::class.java)
        }
    }
}