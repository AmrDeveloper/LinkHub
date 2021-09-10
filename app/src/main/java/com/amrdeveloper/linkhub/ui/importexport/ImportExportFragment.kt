package com.amrdeveloper.linkhub.ui.importexport

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
class ImportExportFragment : Fragment() {

    private var _binding: FragmentImportExportBinding? = null
    private val binding get() = _binding!!

    private val importExportViewModel by viewModels<ImportExportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImportExportBinding.inflate(inflater, container, false)

        setupObservers()

        binding.importAction.setOnClickListener {
            importDataFile()
        }

        binding.exportAction.setOnClickListener {
            exportDataFile()
        }

        return binding.root
    }

    private fun setupObservers() {
        importExportViewModel.errorMessages.observe(viewLifecycleOwner, { messageId ->
            activity.showSnackBar(messageId)
        })
    }

    private fun importDataFile() {
        importFileFromDeviceWithPermission()
    }

    private fun exportDataFile() {
        exportFileFromDeviceWthPermission()
    }

    private fun importFileFromDeviceWithPermission() {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPGranted = checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

            if (readPGranted) launchFileChooserIntent()
            else permissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        } else {
            launchFileChooserIntent()
        }
    }

    private fun exportFileFromDeviceWthPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            importExportViewModel.exportDataFile(requireContext())
        }
         else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPGranted = checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

            if (readPGranted) importExportViewModel.exportDataFile(requireContext())
            else permissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        } else {
            importExportViewModel.exportDataFile(requireContext())
        }
    }

//    @RequiresApi(Build.VERSION_CODES.R)
//    private fun launchStorageManagePermissionRequest() {
//        try {
//            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//            intent.addCategory("android.intent.category.DEFAULT")
//            intent.data = Uri.parse(String.format("package:%s", requireContext().packageName))
//            modernAndroidStorageManageResult.launch(intent)
//        } catch (e: Exception) {
//            val intent = Intent()
//            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//            modernAndroidStorageManageResult.launch(intent)
//        }
//    }

    private fun launchFileChooserIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val chooserIntent = Intent.createChooser(intent, "Select a File to import")
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

//    private val modernAndroidStorageManageResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
//                launchFileChooserIntent()
//            }
//        }

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
                        if (extension != "json") {
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