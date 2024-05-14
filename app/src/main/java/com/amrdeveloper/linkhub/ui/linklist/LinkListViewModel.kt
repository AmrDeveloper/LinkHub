package com.amrdeveloper.linkhub.ui.linklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkListViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _linksLiveData = MutableLiveData<List<Link>>()
    val linksLiveData = _linksLiveData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading = _dataLoading

    private val _errorMessages = MutableLiveData<Int>()
    val errorMessages = _errorMessages

    fun getFolderLinkList(folderId: Int) {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = linkRepository.getSortedFolderLinkList(folderId)
            if (result.isSuccess) {
                _linksLiveData.value = result.getOrDefault(listOf())
            } else {
                _errorMessages.value = R.string.error_get_links
            }
            _dataLoading.value = false
        }
    }

    fun getFolderLinkListByKeyword(folderId: Int, keyword: String) {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = linkRepository.getSortedFolderLinkListByKeyword(folderId, keyword)
            if (result.isSuccess) {
                _linksLiveData.value = result.getOrNull()
            } else {
                _errorMessages.value = R.string.error_get_links
            }
            _dataLoading.value = false
        }
    }

    fun updateLinkClickCount(linkId: Int, count: Int) {
        viewModelScope.launch {
            linkRepository.updateClickCountByLinkId(linkId, count)
        }
    }
}