package com.amrdeveloper.linkhub.ui.folder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.source.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _completeSuccessTask = MutableLiveData<Boolean>()
    val completeSuccessTask = _completeSuccessTask

    private val _errorMessages = MutableLiveData<Int>()
    val errorMessages = _errorMessages

    fun createNewFolder(folder: Folder) {
        viewModelScope.launch {
            val result = folderRepository.insertFolder(folder)
            if (result.isSuccess) {
                if(result.getOrDefault(-1) > 0) _completeSuccessTask.value = true
                else _errorMessages.value = R.string.error_folder_same_name
            } else {
                _errorMessages.value = R.string.error_insert_folder
            }
        }
    }

    fun updateFolder(folder: Folder) {
        viewModelScope.launch {
            val result = folderRepository.updateFolder(folder)
            if (result.isSuccess && result.getOrDefault(-1) > 0) {
                _completeSuccessTask.value = true
            } else {
                _errorMessages.value = R.string.error_update_folder
            }
        }
    }

    fun deleteFolder(folderId: Int) {
        viewModelScope.launch {
            val result = folderRepository.deleteFolderByID(folderId)
            if (result.isSuccess && result.getOrDefault(-1) > 0) {
                _completeSuccessTask.value = true
            } else {
                _errorMessages.value = R.string.error_delete_folder
            }
        }
    }
}