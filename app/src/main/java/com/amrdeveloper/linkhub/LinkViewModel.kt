package com.amrdeveloper.linkhub

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val _currentFolderLiveData = MutableLiveData<Folder>()
    val currentFolderLiveData = _currentFolderLiveData

    private val _foldersLiveData = MutableLiveData<List<Folder>>()
    val folderLiveData = _foldersLiveData

    private val _completeSuccessTask = MutableLiveData<Boolean>()
    val completeSuccessTask = _completeSuccessTask

    private val _errorMessages = MutableLiveData<Int>()
    val errorMessages = _errorMessages

    fun createNewLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.insertLink(link)
            if (result is Result.Success) {
                if(result.data > 0) _completeSuccessTask.value = true
                else _errorMessages.value = R.string.error_link_same_title
            } else {
                _errorMessages.value = R.string.error_insert_link
            }
        }
    }

    fun updateLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.updateLink(link)
            if (result is Result.Success && result.data > 0) {
                _completeSuccessTask.value = true
            } else {
                _errorMessages.value = R.string.error_update_link
            }
        }
    }

    fun deleteLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.deleteLink(link)
            if (result is Result.Success && result.data > 0) {
                _completeSuccessTask.value = true
            } else {
                _errorMessages.value = R.string.error_delete_link
            }
        }
    }

    fun getFolderWithId(folderId : Int) {
        viewModelScope.launch {
            val result = folderRepository.getFolderById(folderId)
            if (result is Result.Success) {
                val folder = result.data
                _currentFolderLiveData.value = folder
            } else {
                _errorMessages.value = R.string.error_get_folders
            }
        }
    }

    fun getFolderList() {
        viewModelScope.launch {
            val result = folderRepository.getSortedFolderList()
            if (result is Result.Success) {
                val folders = result.data
                _foldersLiveData.value = folders
            } else {
                _errorMessages.value = R.string.error_get_folders
            }
        }
    }
}