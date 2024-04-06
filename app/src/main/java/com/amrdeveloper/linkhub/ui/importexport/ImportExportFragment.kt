package com.amrdeveloper.linkhub.ui.importexport

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import com.amrdeveloper.linkhub.data.ImportExportFileType
import com.amrdeveloper.linkhub.databinding.FragmentImportExportBinding
import com.amrdeveloper.linkhub.util.UiPreferences
import com.amrdeveloper.linkhub.util.getFileName
import com.amrdeveloper.linkhub.util.getFileText
import com.amrdeveloper.linkhub.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImportExportFragment : Fragment() {

    private var importExportFileType: ImportExportFileType? = null
    private var _binding: FragmentImportExportBinding? = null
    private val binding get() = _binding!!

    private val importExportViewModel by viewModels<ImportExportViewModel>()

    @Inject
    lateinit var uiPreferences: UiPreferences

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
            launchFileTypePickerDialog(requireContext()) { fileType ->
                importExportFileType = fileType
                importDataFile(fileType)
                if(uiPreferences.isDefaultFolderEnabled())
                    uiPreferences.deleteDefaultFolder()
            }
        }

        binding.exportAction.setOnClickListener {
            launchFileTypePickerDialog(requireContext()) { fileType ->
                importExportFileType = fileType
                exportDataFile(fileType)
            }
        }
    }

     private fun launchFileTypePickerDialog(context: Context, onFileTypeSelected: (ImportExportFileType)->Unit) {
        val fileTypes = ImportExportFileType.entries.map { it.fileTypeName }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.import_export_choose_file_type))
        builder.setItems(fileTypes.toTypedArray()) { dialog, which ->
            val selectedFileType = ImportExportFileType.entries[which]
            onFileTypeSelected(selectedFileType)
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setupObservers() {
        importExportViewModel.stateMessages.observe(viewLifecycleOwner) { messageId ->
            activity.showSnackBar(messageId)
        }
    }

    private fun importDataFile(fileType: ImportExportFileType) {
        importFileFromDeviceWithPermission(fileType)
    }

    private fun exportDataFile(fileType: ImportExportFileType) {
        exportFileFromDeviceWthPermission(fileType)
    }

    private fun importFileFromDeviceWithPermission(fileType: ImportExportFileType) {
        // From Android 33 no need for READ_EXTERNAL_STORAGE permission for non media files
        if (Build.VERSION_CODES.TIRAMISU > Build.VERSION.SDK_INT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPermissionState = checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            if (readPermissionState == PackageManager.PERMISSION_GRANTED) launchFileChooserIntent(fileType)
            else permissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        } else {
            launchFileChooserIntent(fileType)
        }
    }

    private fun exportFileFromDeviceWthPermission(fileType: ImportExportFileType) {
        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPermissionState = checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (readPermissionState == PackageManager.PERMISSION_GRANTED)
                importExportViewModel.exportDataFile(requireContext(), fileType)
            else permissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        } else {
            importExportViewModel.exportDataFile(requireContext(), fileType)
        }
    }

    private fun launchFileChooserIntent(fileType: ImportExportFileType) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = fileType.mimeType
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val chooserIntent = Intent.createChooser(intent, "Select a File to import")
        loadFileActivityResult.launch(chooserIntent)
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            importExportFileType?.let { fileType->
                if(result[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                    importExportViewModel.exportDataFile(requireContext(), fileType)
                }
                else if(result[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                    launchFileChooserIntent(fileType)
                }
            }
        }

    private val loadFileActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultIntent = result.data
                if(resultIntent != null) {
                    val fileUri = resultIntent.data
                    if(fileUri != null) {
                        importExportFileType?.let { fileType->
                            val contentResolver =  requireActivity().contentResolver
                            val fileName = contentResolver.getFileName(fileUri)
                            val extension = fileName.substring(fileName.lastIndexOf('.') + 1)
                            if (".$extension" != fileType.extension) {
                                activity?.showSnackBar(R.string.message_invalid_extension)
                                return@registerForActivityResult
                            }
                            val fileContent = contentResolver.getFileText(fileUri)
                            importExportViewModel.importDataFile(fileContent, fileType)
                        }
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}