package com.getir.patika.foodcouriers.presentation.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.getir.patika.foodcouriers.MainActivity
import com.getir.patika.foodcouriers.common.domain.ViewState
import com.getir.patika.foodcouriers.data.local.DataStoreManager
import com.getir.patika.foodcouriers.databinding.FragmentLoginAccountBinding
import com.getir.patika.foodcouriers.domain.model.BaseResponse
import com.getir.patika.foodcouriers.domain.model.Login
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginAccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()
    private lateinit var binding: FragmentLoginAccountBinding

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginAccountBinding.inflate(layoutInflater)
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

    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            viewModel.setLogin(Login(etEmail.text.toString(), etPassword.text.toString()))
          //  progress.visibility = View.VISIBLE
        }
    }

    private fun initObserver() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiStateLogin.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            Log.d("LoginFragment", response.data)
//                            binding.progress.visibility = View.GONE
                            dataStoreManager.saveUserId(response.data)
                            startActivity(MainActivity.callIntent(requireContext()))
                        }

                        is ViewState.Error -> {
                            val response = viewState.error
                            Log.d("LoginFragment", "Error: $response")
//                            binding.progress.visibility = View.GONE
                        }

                        is ViewState.Loading -> {
                            Log.d("LoginFragment", "Loading")
//                            binding.progress.visibility = View.GONE

                        }
                    }
                }
        }
    }

}