package com.amrdeveloper.linkhub.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val _folderLiveData = MutableLiveData<List<Folder>>()
    val folderLiveData = _folderLiveData

    private val _linkLiveData = MutableLiveData<List<Link>>()
    val linkLiveData = _linkLiveData

    private val _errorMessages = MutableLiveData<Int>()
    val errorMessages = _errorMessages

    fun getTopLimitedFolders(limit: Int) {
        viewModelScope.launch {
            val result = folderRepository.getLimitedSortedFolderList(limit)
            if (result.isSuccess) {
                _folderLiveData.value = result.getOrDefault(listOf())
            } else {
                _errorMessages.value = R.string.error_get_top_folders
            }
        }
    }

    fun updateFolderClickCount(folderId: Int, count: Int) {
        viewModelScope.launch {
            folderRepository.updateClickCountByFolderId(folderId, count)
        }
    }

    fun insertLink(link: Link) {
        viewModelScope.launch {
            linkRepository.insertLink(link)
        }
    }

    fun getSortedLinks() {
        viewModelScope.launch {
            val result = linkRepository.getSortedLinkList()
            if (result.isSuccess) {
                _linkLiveData.value = result.getOrDefault(listOf())
            } else {
                _errorMessages.value = R.string.error_get_links
            }
        }
    }

    fun getSortedLinksByKeyword(keyword: String) {
        viewModelScope.launch {
            val result = linkRepository.getSortedLinkListByKeyword(keyword)
            if (result.isSuccess) {
                _linkLiveData.value = result.getOrDefault(listOf())
            } else {
                _errorMessages.value = R.string.error_get_links
            }
        }
    }

    suspend fun getFolderById(id : Int) : Result<Folder> {
        return folderRepository.getFolderById(id)
    }

    fun updateLinkClickCount(linkId: Int, count: Int) {
        viewModelScope.launch {
            linkRepository.updateClickCountByLinkId(linkId, count)
        }
    }

    fun deleteLink(link: Link) {
        viewModelScope.launch {
            linkRepository.deleteLink(link)
            _linkLiveData.value?.toMutableList()?.remove(link)
        }
    }
}