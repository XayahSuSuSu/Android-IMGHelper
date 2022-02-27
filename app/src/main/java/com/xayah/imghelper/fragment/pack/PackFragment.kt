package com.xayah.imghelper.fragment.pack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xayah.imghelper.R
import com.xayah.imghelper.databinding.PackFragmentBinding
import com.xayah.imghelper.util.Tool
import com.xayah.materialyoufileexplorer.MaterialYouFileExplorer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PackFragment : Fragment() {
    private lateinit var binding: PackFragmentBinding
    private lateinit var fileExplorer: MaterialYouFileExplorer
    private lateinit var dirExplorer: MaterialYouFileExplorer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PackFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileExplorer = MaterialYouFileExplorer()
        dirExplorer = MaterialYouFileExplorer()
        fileExplorer.initialize(this)
        dirExplorer.initialize(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = ViewModelProvider(this)[PackViewModel::class.java]
        binding.largeActionCardBoot.setOnClickListener {
            packBOOT()
        }
    }

    fun packBOOT() {
        fileExplorer.toExplorer(requireContext(), true) { imgPath, _ ->
            if (imgPath != "") {
                dirExplorer.toExplorer(requireContext(), false) { outPath, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val pack = Tool.packBoot(imgPath, outPath)
                        withContext(Dispatchers.Main) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.tips))
                                .setMessage(
                                    if (pack) getString(R.string.pack_successfully) + outPath else getString(
                                        R.string.pack_failed
                                    )
                                )
                                .setPositiveButton(getString(R.string.confirm)) { _, _ -> }
                                .show()
                        }
                    }
                }
            }
        }
    }

}