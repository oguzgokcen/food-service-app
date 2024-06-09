package com.getir.patika.foodcouriers.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.databinding.FragmentOnboardingCheapBinding
import com.getir.patika.foodcouriers.databinding.FragmentOnboardingFavoritiesBinding
import com.wada811.viewbindingktx.viewBinding

class OnboardingCheapFragment : Fragment(R.layout.fragment_onboarding_cheap) {

    private val binding by viewBinding(FragmentOnboardingCheapBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btNext.setOnClickListener {
            if(isAdded) {
                findNavController().navigate(R.id.action_onboardingCheapFragment_to_accountFragment)
            }
        }
    }

    companion object {

    }
}