package com.xayah.imghelper.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xayah.imghelper.R
import com.xayah.imghelper.databinding.HomeFragmentBinding
import com.xayah.imghelper.util.Tool
import com.xayah.materialyoufileexplorer.MaterialYouFileExplorer

class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding
    private lateinit var dirExplorer: MaterialYouFileExplorer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dirExplorer = MaterialYouFileExplorer()
        dirExplorer.initialize(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.largeActionCardExtract.setOnClickListener {
            dirExplorer.toExplorer(
                requireContext(),
                false,
                getString(R.string.choose_save_path)
            ) { outPath, _ ->
                val items: Array<String> = arrayOf("boot", "dtbo", "recovery")
                var choice = 0
                val context = requireContext()
                MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.extract_img))
                    .setCancelable(true)
                    .setSingleChoiceItems(
                        items,
                        0
                    ) { _, which ->
                        choice = which
                    }
                    .setPositiveButton(context.getString(R.string.confirm)) { _, _ ->
                        val block = Tool.findBlock(items[choice])
                        var isSuccess = false
                        if (block != "") {
                            isSuccess = Tool.extractImage(block, "$outPath/${items[choice]}.img")
                        }
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.tips))
                            .setMessage(
                                if (isSuccess) getString(R.string.extract_successfully) else getString(
                                    R.string.extract_failed
                                )
                            )
                            .setPositiveButton(getString(R.string.confirm)) { _, _ -> }
                            .show()
                    }
                    .show()
            }
        }

    }

}