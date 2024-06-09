package com.getir.patika.foodcouriers.presentation.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.databinding.FragmentPaymentBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.PaymentModels.Payment
import com.getir.patika.foodcouriers.presentation.HomeViewModel
import com.getir.patika.foodcouriers.presentation.orderdetails.FinishOrderFragment
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    val binding by viewBinding(FragmentPaymentBinding::bind)
    private val viewModel: HomeViewModel by activityViewModels()

    private val args: PaymentFragmentArgs by navArgs()
    var orderId: Int=0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderId=args.orderId
        binding.apply {
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.months_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCardExpMonth.adapter = adapter
            }

            // Set up year spinner
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.years_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCardExpYear.adapter = adapter
            }

            btCompletePayment.setOnClickListener {
                val cardNumber = etCardNo.text.toString().toLongOrNull()
                val expMonth = spinnerCardExpMonth.selectedItem.toString().toInt()
                val expYear = spinnerCardExpYear.selectedItem.toString().toInt()
                val cvv = etCvv.text.toString().toIntOrNull()

                if (cardNumber != null && cvv != null) {
                    val payment = Payment(orderId,cardNumber, expMonth, expYear, cvv)
                    loadingView.visibility = View.VISIBLE
                    viewModel.doPayment(payment)

                } else {
                    // Show error message if inputs are invalid
                    Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
                }
            }
        }
        initObserver()
    }

    fun initObserver(){
        with(viewModel){
            viewLifecycleOwner.lifecycleScope.launch {
                MakePaymentStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { viewState ->
                        when (viewState) {
                            is ViewState.Success -> {
                                val response = viewState.result as BaseResponse.Success
                                Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_SHORT).show()
                                viewModel.emptyCard()
                                binding.loadingView.visibility = View.GONE
                                val intent = FinishOrderFragment.callIntent(requireActivity())
                                intent.putExtra("orderId",orderId)
                                startActivity(intent)
                            }

                            is ViewState.Error -> {
                                val responseError = viewState.error
                                binding.loadingView.visibility = View.GONE
                                Log.v("ViewState.Error", responseError)
                            }

                            is ViewState.Loading -> {
                                Log.v("ViewState.Loading", "ViewState.Loading")
                            }
                        }
                    }
            }
        }
    }
    private fun validateCardNumber(cardNumber: String) {
        val cardNumberPattern = Regex("^\\d{16}\$")
        if (!cardNumber.matches(cardNumberPattern)) {
            // Show error
        }
    }

    private fun validateCvv(cvv: String) {
        val cvvPattern = Regex("^\\d{3,4}\$")
        if (!cvv.matches(cvvPattern)) {
            // Show error
        }
    }
}