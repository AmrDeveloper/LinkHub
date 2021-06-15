package com.amrdeveloper.linkhub

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result
import com.amrdeveloper.linkhub.data.source.LinkRepository
import kotlinx.coroutines.launch

class LinkListViewModel(
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _linksLiveData = MutableLiveData<List<Link>>()
    val linksLiveData = _linksLiveData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading = _dataLoading

    private val _errorMessages = MutableLiveData<Int>()
    val errorMessages = _errorMessages

    fun getFolderLinkList(folderId : Int) {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = linkRepository.getSortedFolderLinkList(folderId)
            if(result is Result.Success) {
                val links = result.data
                _linksLiveData.value = links
            } else {
                _errorMessages.value = R.string.error_get_links
            }
            _dataLoading.value = false
        }
    }

    fun getFolderLinkListByKeyword(folderId: Int, keyword : String) {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = linkRepository.getSortedFolderLinkListByKeyword(folderId, keyword)
            if(result is Result.Success) {
                val links = result.data
                _linksLiveData.value = links
            } else {
                _errorMessages.value = R.string.error_get_links
            }
            _dataLoading.value = false
        }
    }

}

class LinkListViewModelFactory(
    private val linkRepository: LinkRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LinkListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LinkListViewModel(linkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}