package com.getir.patika.foodcouriers.presentation.account

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.databinding.FragmentCreateAccountBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Register
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()
    private lateinit var binding: FragmentCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initListener()

    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        // En az bir büyük harf ve bir özel karakter içeriyor mu kontrol et
        val regex = "(?=.*[A-Z])(?=.*[\\W_]).+"
        return password.matches(regex.toRegex())&&password.length>7
    }


    private fun initListener() {
        binding.apply {
            btnSignUp.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (etName.text.toString().isEmpty()) {
                    binding.tvInputName.error = getString(R.string.please_enter_your_name)
                    return@setOnClickListener
                } else {
                    binding.tvInputName.error = null
                }

                if (!validateEmail(email)) {
                    binding.tvInputEmail.error =
                        getString(R.string.please_enter_a_valid_email_address)
                    return@setOnClickListener
                } else {
                    binding.tvInputEmail.error = null
                }

                if (!validatePassword(password)) {
                    binding.tvInputPassword.error =
                        getString(R.string.your_password_must_contain_at_least_one_uppercase_letter_and_a_special_character)
                    return@setOnClickListener
                } else {
                    binding.tvInputPassword.error = null
                }
                val register = Register(
                    etEmail.text.toString(),
                    etName.text.toString(),
                    etPassword.text.toString()
                )
                viewModel.setRegister(register)
            }
        }
    }

    private fun initObserver() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiStateRegister.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            Toast.makeText(context, response.data.message, Toast.LENGTH_SHORT).show()
                        }

                        is ViewState.Error -> {
                            val response = viewState.error
                            Log.d("CreateAccountFragment", "Error: $response")
                        }

                        is ViewState.Loading -> {
                            Log.d("CreateAccountFragment", "Loading")
                        }
                    }
                }
        }
    }

}