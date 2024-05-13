package com.getir.patika.foodcouriers.presentation.food

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.databinding.FragmentDetailMenuBinding
import com.wada811.viewbindingktx.viewBinding


class DetailMenuFragment : Fragment(R.layout.fragment_detail_menu) {
    private val binding by viewBinding(FragmentDetailMenuBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    companion object {

    }
}