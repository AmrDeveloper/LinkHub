package com.amrdeveloper.linkhub.ui.explorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.common.LazyValue
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class QueryParam(
    val folderId: Int,
    val query: String
)

@HiltViewModel
class LinkListViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow(value = "")
    private val currentFolderId = MutableStateFlow(value = -1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val sortedFoldersState: StateFlow<LazyValue<List<Folder>>> =
        combine(currentFolderId, searchQuery) { folderId, query ->
            QueryParam(folderId, query)
        }.flatMapLatest { params ->
            folderRepository.getSortedFolders(
                keyword = params.query,
                folderId = params.folderId
            )
        }.map { LazyValue(data = it, isLoading = false) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LazyValue(data = listOf(), isLoading = true)
        )

    val sortedLinksState: StateFlow<LazyValue<List<Link>>> =
        combine(currentFolderId, searchQuery) { folderId, query ->
            QueryParam(folderId, query)
        }.flatMapLatest { params ->
            linkRepository.getSortedLinks(keyword = params.query, folderId = params.folderId)
        }.map {
            LazyValue(data = it, isLoading = false)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LazyValue(data = listOf(), isLoading = true)
        )

    fun updateFolderId(folderId: Int) {
        currentFolderId.value = folderId
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun incrementFolderClickCount(folder: Folder) {
        viewModelScope.launch {
            folderRepository.updateClickCountByFolderId(folder.id, folder.clickedCount.plus(1))
        }
    }

    fun incrementLinkClickCount(link: Link) {
        viewModelScope.launch {
            linkRepository.updateClickCountByLinkId(link.id, link.clickedCount.plus(1))
        }
    }
}