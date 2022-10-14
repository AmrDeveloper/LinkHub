package com.amrdeveloper.linkhub.ui.link

import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.LinkInfo
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.util.generateLinkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _linkInfoLiveData = MutableLiveData<LinkInfo>()
    val linkInfoLiveData = _linkInfoLiveData

    private val _completeSuccessTask = MutableLiveData<Boolean>()
    val completeSuccessTask = _completeSuccessTask

    private val _errorMessages = MutableLiveData<Int>()
    val errorMessages = _errorMessages

    fun createNewLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.insertLink(link)
            if (result.isSuccess) {
                if(result.getOrDefault(-1) > 0) _completeSuccessTask.value = true
                else _errorMessages.value = R.string.error_link_same_title
            } else {
                _errorMessages.value = R.string.error_insert_link
            }
        }
    }

    fun updateLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.updateLink(link)
            if (result.isSuccess && result.getOrDefault(-1) > 0) {
                _completeSuccessTask.value = true
            } else {
                _errorMessages.value = R.string.error_update_link
            }
        }
    }

    fun deleteLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.deleteLink(link)
            if (result.isSuccess && result.getOrDefault(-1) > 0) {
                _completeSuccessTask.value = true
            } else {
                _errorMessages.value = R.string.error_delete_link
            }
        }
    }

    fun generateLinkTitleAndSubTitle(url : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isValidLink =  URLUtil.isValidUrl(url)
            if (isValidLink.not()) return@launch
            val linkInfo = generateLinkInfo(url)
            withContext(Dispatchers.Main) {
                linkInfoLiveData.value = linkInfo
            }
        }
    }

    fun getFolderWithId(folderId : Int) {
        viewModelScope.launch {
            val result = folderRepository.getFolderById(folderId)
            if (result.isSuccess) {
                _currentFolderLiveData.value = result.getOrNull()
            } else {
                _errorMessages.value = R.string.error_get_folders
            }
        }
    }

    fun getFolderList() {
        viewModelScope.launch {
            val result = folderRepository.getSortedFolderList()
            if (result.isSuccess) {
                _foldersLiveData.value = result.getOrDefault(listOf())
            } else {
                _errorMessages.value = R.string.error_get_folders
            }
        }
    }
}