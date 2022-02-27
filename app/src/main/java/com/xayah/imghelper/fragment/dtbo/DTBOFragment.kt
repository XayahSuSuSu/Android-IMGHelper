package com.xayah.imghelper.fragment.dtbo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xayah.imghelper.R
import com.xayah.imghelper.databinding.DtboFragmentBinding
import com.xayah.imghelper.util.Tool
import com.xayah.materialyoufileexplorer.MaterialYouFileExplorer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DTBOFragment : Fragment() {
    private lateinit var binding: DtboFragmentBinding
    private lateinit var fileExplorer: MaterialYouFileExplorer
    private lateinit var dirExplorer: MaterialYouFileExplorer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DtboFragmentBinding.inflate(inflater, container, false)
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
        binding.viewModel = ViewModelProvider(this)[DTBOViewModel::class.java]
        binding.textButtonPatch.setOnClickListener {
            patchDTBO()
        }
        binding.filledButtonPatchFlash.setOnClickListener {
            patchDTBO(true)
        }
        binding.textInputEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString() == "") {
                binding.textButtonPatch.isEnabled = false
                binding.filledButtonPatchFlash.isEnabled = false
            } else {
                binding.textButtonPatch.isEnabled = true
                binding.filledButtonPatchFlash.isEnabled = true
            }
        }
    }

    fun patchDTBO(flash: Boolean = false) {
        fileExplorer.toExplorer(requireContext(), true) { imgPath, _ ->
            if (imgPath != "") {
                dirExplorer.toExplorer(requireContext(), false) { outPath, _ ->
                    val builder = MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.wait))
                        .setCancelable(false)
                        .show()
                    val mParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    mParam.setMargins(0, 200, 0, 100)
                    builder.addContentView(ProgressBar(requireContext()), mParam)
                    val params: WindowManager.LayoutParams = builder.window!!.attributes
                    builder.window!!.attributes = params
                    CoroutineScope(Dispatchers.IO).launch {
                        val patch = Tool.patchDTBO(
                            requireContext(),
                            binding.textInputEditText.text.toString(),
                            imgPath,
                            outPath
                        )
                        withContext(Dispatchers.Main) {
                            builder.dismiss()
                            if (patch) {
                                val block = Tool.findBlock("dtbo")
                                if (block != "") {
                                    Tool.flashImage("$outPath/dtbo_new.img", block)
                                }
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle(getString(R.string.tips))
                                    .setMessage(
                                        getString(R.string.patch_dtbo_successfully) + outPath + if (block != "") getString(
                                            R.string.patch_dtbo_flash_successfully
                                        ) else getString(R.string.patch_dtbo_flash_failed)
                                    )
                                    .setPositiveButton(getString(R.string.confirm)) { _, _ -> }
                                    .show()
                            } else {
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle(getString(R.string.tips))
                                    .setMessage(getString(R.string.patch_dtbo_failed))
                                    .setPositiveButton(getString(R.string.confirm)) { _, _ -> }
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

}