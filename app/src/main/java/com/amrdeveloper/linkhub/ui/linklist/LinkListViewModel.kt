package com.amrdeveloper.linkhub.ui.linklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LinksUiState(
    var links: List<Link> = listOf(),
    var isLoading: Boolean = false
)

private data class QueryParam(
    val folderId: Int,
    val query: String
)

@HiltViewModel
class LinkListViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow(value = "")
    private val currentFolderId = MutableStateFlow(value = -1)
    val uiState: StateFlow<LinksUiState> =
        combine(currentFolderId, searchQuery) { folderId, query ->
            QueryParam(folderId, query)
        }.flatMapLatest { queryParam ->
            if (queryParam.query.isEmpty()) {
                linkRepository.getSortedFolderLinkListByKeywordFlow(
                    id = queryParam.folderId,
                    keyword = queryParam.query
                )
            } else {
                linkRepository.getSortedFolderLinkListFlow(id = queryParam.folderId)
            }
        }.map {
            LinksUiState(links = it, isLoading = false)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LinksUiState(isLoading = true)
        )

    fun updateFolderId(folderId: Int) {
        currentFolderId.value = folderId
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun incrementLinkClickCount(link: Link) {
        viewModelScope.launch {
            linkRepository.updateClickCountByLinkId(link.id, link.clickedCount.plus(1))
        }
    }
}