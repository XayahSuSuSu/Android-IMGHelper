package com.xayah.imghelper.fragment.dtbo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xayah.imghelper.databinding.DtboFragmentBinding

class DTBOFragment : Fragment() {
    private lateinit var binding: DtboFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DtboFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = ViewModelProvider(this)[DTBOViewModel::class.java]
    }

}