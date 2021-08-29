package com.amrdeveloper.linkhub.ui.importexport

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.DataPackage
import com.amrdeveloper.linkhub.data.Result
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
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
) : ViewModel() {

    private val _errorMessages = MutableLiveData<Int>()
    val errorMessages = _errorMessages

    fun importDataFile(data : String) {
        viewModelScope.launch {
            try {
                val dataPackage = Gson().fromJson(data, DataPackage::class.java)
                val folders = dataPackage.folders
                val links = dataPackage.links

                folderRepository.insertFolders(folders)
                linkRepository.insertLinks(links)
                _errorMessages.value = R.string.message_data_imported
            } catch (e : JsonSyntaxException) {
                _errorMessages.value = R.string.message_invalid_data_format
            }
        }
    }

    fun exportDataFile(context: Context) {
        viewModelScope.launch {
            val foldersResult = folderRepository.getFolderList()
            val linksResult = linkRepository.getLinkList()
            if ((foldersResult is Result.Success) && (linksResult is Result.Success)) {
                val folders = foldersResult.data
                val links = linksResult.data
                val dataPackage = DataPackage(folders, links)
                val jsonDataPackage = Gson().toJson(dataPackage)
                createdExportedFile(context, jsonDataPackage)
            } else {
                _errorMessages.value = R.string.message_invalid_export
            }
        }
    }

    private fun createdExportedFile(context: Context, data : String) {
        val fileName = System.currentTimeMillis().toString() + ".json"
        val dataExternalDir = context.getExternalFilesDir("data")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, dataExternalDir?.path)
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
            val outputStream = uri?.let { resolver.openOutputStream(it) }
            outputStream?.write(data.toByteArray())
        } else {
            val dataFile = File(dataExternalDir, fileName)
            dataFile.writeText(data)
        }

        _errorMessages.value = R.string.message_data_exported
    }

}