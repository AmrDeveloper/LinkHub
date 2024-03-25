package com.amrdeveloper.linkhub.ui.importexport

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.ImportExportFileType
import com.amrdeveloper.linkhub.data.parser.HtmlImportExportFileParser
import com.amrdeveloper.linkhub.data.parser.ImportExportFileParser
import com.amrdeveloper.linkhub.data.parser.JsonImportExportFileParser
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImportExportViewModel @Inject constructor (
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository,
    private val uiPreferences: UiPreferences,
) : ViewModel() {

    private val _stateMessages = MutableLiveData<Int>()
    private var importExportFileParser: ImportExportFileParser = JsonImportExportFileParser()
    val stateMessages = _stateMessages

    val mimeType: String
        get() =
            importExportFileParser.getFileType().mimeType
    val extension: String
        get() =
            importExportFileParser.getFileType().extension

    fun setFileType(fileType: ImportExportFileType){
        importExportFileParser = when(fileType){
            ImportExportFileType.JSON-> JsonImportExportFileParser()
            ImportExportFileType.HTML-> HtmlImportExportFileParser()
        }
    }

    fun importDataFile(data : String) {
        viewModelScope.launch {
                val dataPackageResult = importExportFileParser.importData(data, folderRepository, linkRepository)
                //dataPackage is null in case of non-configuration import
                if(dataPackageResult.isSuccess) {
                    dataPackageResult.getOrNull()?.let {
                        // Import show click count flag if it available
                        val lastShowClickCountConfig = uiPreferences.isClickCounterEnabled()
                        uiPreferences.setEnableClickCounter(
                            it.showClickCounter ?: lastShowClickCountConfig
                        )
                        // Import enabling auto saving
                        val lastAutoSavingEnabled = uiPreferences.isAutoSavingEnabled()
                        uiPreferences.setEnableAutoSave(
                            it.enableAutoSaving ?: lastAutoSavingEnabled
                        )
                        // Import theme flag if it available
                        val lastThemeOption = uiPreferences.getThemeType()
                        uiPreferences.setThemeType(it.theme ?: lastThemeOption)
                    }
                    _stateMessages.value = R.string.message_data_imported
                } else {
                    _stateMessages.value = R.string.message_invalid_data_format
                }
        }
    }

    fun exportDataFile(context: Context) {
        viewModelScope.launch {
            val exportResult = importExportFileParser.exportData(folderRepository, linkRepository, uiPreferences)
            if (exportResult.isSuccess) {
                createdExportedFile(context, exportResult.getOrDefault(""))
            } else {
                _stateMessages.value = R.string.message_invalid_export
            }
        }
    }

    private fun createdExportedFile(context: Context, data : String) {
        val fileName = System.currentTimeMillis().toString() + importExportFileParser.getFileType().extension

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, importExportFileParser.getFileType().mimeType)
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            val outputStream = uri?.let { resolver.openOutputStream(it) }
            outputStream?.write(data.toByteArray())
        } else {
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val dataFile = File(downloadDir, fileName)
            dataFile.writeText(data)
        }

        _stateMessages.value = R.string.message_data_exported
    }

}