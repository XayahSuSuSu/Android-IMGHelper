package com.xayah.imghelper.fragment.unpack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xayah.imghelper.R
import com.xayah.imghelper.databinding.UnpackFragmentBinding
import com.xayah.imghelper.util.Tool
import com.xayah.materialyoufileexplorer.MaterialYouFileExplorer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnpackFragment : Fragment() {
    private lateinit var binding: UnpackFragmentBinding
    private lateinit var fileExplorer: MaterialYouFileExplorer
    private lateinit var dirExplorer: MaterialYouFileExplorer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UnpackFragmentBinding.inflate(inflater, container, false)
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
        binding.viewModel = ViewModelProvider(this)[UnpackViewModel::class.java]
        binding.largeActionCardBoot.setOnClickListener {
            unpackBOOT()
        }
    }

    fun unpackBOOT() {
        fileExplorer.toExplorer(
            requireContext(),
            true,
            getString(R.string.choose_boot_unpack)
        ) { imgPath, _ ->
            if (imgPath != "") {
                dirExplorer.toExplorer(
                    requireContext(),
                    false,
                    getString(R.string.choose_save_path)
                ) { outPath, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val unpack = Tool.unpackBoot(imgPath, outPath)
                        withContext(Dispatchers.Main) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.tips))
                                .setMessage(
                                    if (unpack) getString(R.string.unpack_successfully) + outPath else getString(
                                        R.string.unpack_failed
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