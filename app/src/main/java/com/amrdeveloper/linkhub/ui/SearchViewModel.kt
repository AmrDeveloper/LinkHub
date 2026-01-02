package com.amrdeveloper.linkhub.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.common.LazyValue
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.ui.components.SearchParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository,
) : ViewModel() {

    // TODO: Support isPinned and other options

    private val searchQuery = MutableStateFlow(value = "")
    private val searchParams = MutableStateFlow(value = SearchParams())

    @OptIn(ExperimentalCoroutinesApi::class)
    val sortedFoldersState: StateFlow<LazyValue<List<Folder>>> =
        combine(searchQuery) {
            searchQuery.value
        }.flatMapLatest { query ->
            if (query.isEmpty()) folderRepository.getSortedFolderListFlow()
            else folderRepository.getSortedFolderListByKeywordFlow(query)
        }.map { LazyValue(data = it, isLoading = false) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LazyValue(data = listOf(), isLoading = true)
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val sortedLinksState: StateFlow<LazyValue<List<Link>>> =
        combine(searchQuery) {
            searchQuery.value
        }.flatMapLatest { query ->
            if (query.isEmpty()) linkRepository.getSortedLinkList()
            else linkRepository.getSortedLinkListByKeyword(query)
        }.map {
            LazyValue(data = it, isLoading = false)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LazyValue(data = listOf(), isLoading = true)
        )

    fun updateSearchParams(query : String, params: SearchParams) {
        searchQuery.value = query
        searchParams.value = params
    }
}