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
import com.amrdeveloper.linkhub.data.DataPackage
import com.amrdeveloper.linkhub.data.FolderColor
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.util.UiPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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
    val stateMessages = _stateMessages

    fun importDataFile(data : String) {
        viewModelScope.launch {
            try {
                val dataPackage = Gson().fromJson(data, DataPackage::class.java)

                val folders = dataPackage.folders
                // This code should be removed after found why it not serialized on some devices (see Issue #23)
                // folderColor field is declared as non nullable type but in this case GSON will break the null safty feature
                folders.forEach { if (it.folderColor == null) it.folderColor = FolderColor.BLUE }
                folderRepository.insertFolders(folders)

                linkRepository.insertLinks(dataPackage.links)

                // Import show click count flag if it available
                val lastShowClickCountConfig = uiPreferences.isClickCounterEnabled()
                uiPreferences.setEnableClickCounter(dataPackage.showClickCounter ?: lastShowClickCountConfig)

                // Import enabling auto saving
                val lastAutoSavingEnabled = uiPreferences.isAutoSavingEnabled()
                uiPreferences.setEnableAutoSave(dataPackage.enableAutoSaving ?: lastAutoSavingEnabled)

                // Import theme flag if it available
                val lastThemeOption = uiPreferences.getThemeType()
                uiPreferences.setThemeType(dataPackage.theme ?: lastThemeOption)

                _stateMessages.value = R.string.message_data_imported
            } catch (e : JsonSyntaxException) {
                _stateMessages.value = R.string.message_invalid_data_format
            }
        }
    }

    fun exportDataFile(context: Context) {
        viewModelScope.launch {
            val foldersResult = folderRepository.getFolderList()
            val linksResult = linkRepository.getLinkList()
            if (foldersResult.isSuccess && linksResult.isSuccess) {
                val folders = foldersResult.getOrDefault(listOf())
                val links = linksResult.getOrDefault(listOf())
                val showClickCounter = uiPreferences.isClickCounterEnabled()
                val autoSaving = uiPreferences.isAutoSavingEnabled()
                val lastTheme = uiPreferences.getThemeType()
                val dataPackage = DataPackage(folders, links, showClickCounter, autoSaving, lastTheme)
                val jsonDataPackage = Gson().toJson(dataPackage)
                createdExportedFile(context, jsonDataPackage)
            } else {
                _stateMessages.value = R.string.message_invalid_export
            }
        }
    }

    private fun createdExportedFile(context: Context, data : String) {
        val fileName = System.currentTimeMillis().toString() + ".json"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            val outputStream = uri?.let { resolver.openOutputStream(it) }
            outputStream?.write(data.toByteArray())
        } else {
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            val dataFile = File(downloadDir, fileName)
            dataFile.writeText(data)
        }

        _stateMessages.value = R.string.message_data_exported
    }

}