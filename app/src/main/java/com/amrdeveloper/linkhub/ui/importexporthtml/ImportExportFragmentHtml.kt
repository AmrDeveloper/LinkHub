package com.amrdeveloper.linkhub.ui.importexport

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.databinding.FragmentImportExportBinding
import com.amrdeveloper.linkhub.util.getFileName
import com.amrdeveloper.linkhub.util.getFileText
import com.amrdeveloper.linkhub.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImportExportFragmentHtml : Fragment() {

    private var _binding: FragmentImportExportBinding? = null
    private val binding get() = _binding!!

    private val importExportViewModel by viewModels<ImportExportHtmlViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImportExportBinding.inflate(inflater, container, false)

        setupListeners()
        setupObservers()

        return binding.root
    }

    private fun setupListeners() {
        binding.importAction.setOnClickListener {
            importDataFile()
        }

        binding.exportAction.setOnClickListener {
            exportDataFile()
        }
    }

    private fun setupObservers() {
        importExportViewModel.stateMessages.observe(viewLifecycleOwner) { messageId ->
            activity.showSnackBar(messageId)
        }
    }

    private fun importDataFile() {
        importFileFromDeviceWithPermission()
    }

    private fun exportDataFile() {
        exportFileFromDeviceWthPermission()
    }

    private fun importFileFromDeviceWithPermission() {
        // From Android 33 no need for READ_EXTERNAL_STORAGE permission for non media files
        if (Build.VERSION_CODES.TIRAMISU > Build.VERSION.SDK_INT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPermissionState = checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            if (readPermissionState == PackageManager.PERMISSION_GRANTED) launchFileChooserIntent()
            else permissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        } else {
            launchFileChooserIntent()
        }
    }

    private fun exportFileFromDeviceWthPermission() {
        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPermissionState = checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (readPermissionState == PackageManager.PERMISSION_GRANTED) importExportViewModel.exportDataFile(requireContext())
            else permissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        } else {
            importExportViewModel.exportDataFile(requireContext())
        }
    }

    private fun launchFileChooserIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "text/html"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val chooserIntent = Intent.createChooser(intent, "Select a html file File to import")
        loadFileActivityResult.launch(chooserIntent)
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if(result[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                importExportViewModel.exportDataFile(requireContext())
            }
            else if(result[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                launchFileChooserIntent()
            }
        }

    private val loadFileActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultIntent = result.data
                if(resultIntent != null) {
                    val fileUri = resultIntent.data
                    if(fileUri != null) {
                        val contentResolver =  requireActivity().contentResolver
                        val fileName = contentResolver.getFileName(fileUri)
                        val extension = fileName.substring(fileName.lastIndexOf('.') + 1)
                        if (extension != "html") {
                            activity?.showSnackBar(R.string.message_invalid_extension)
                            return@registerForActivityResult
                        }
                        val fileContent = contentResolver.getFileText(fileUri)
                        importExportViewModel.importDataFile(fileContent)
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}