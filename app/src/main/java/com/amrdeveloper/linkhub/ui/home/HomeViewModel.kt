package com.amrdeveloper.linkhub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.amrdeveloper.linkhub.common.LazyValue
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.SortingOptions
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

private const val NUMBER_OF_TOP_FOLDERS = 6

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val sortingOption = MutableStateFlow(value = SortingOptions.DEFAULT)

    val mostUsedLimitedFoldersState =
        Pager(config = PagingConfig(pageSize = NUMBER_OF_TOP_FOLDERS, prefetchDistance = 2)) {
            folderRepository.getMoseUsedFoldersWithPagination()
        }.flow.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val sortedLinksState: StateFlow<LazyValue<List<Link>>> =
        combine(sortingOption) {
            sortingOption.value
        }.flatMapLatest { option ->
            linkRepository.getSortedLinks(sortingOption = option)
        }.map { LazyValue(data = it, isLoading = false) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LazyValue(data = listOf(), isLoading = true)
        )

    fun setLinksSortOption(option: SortingOptions) {
        sortingOption.value = option
    }

    fun incrementLinkClickCount(link: Link) {
        viewModelScope.launch {
            linkRepository.updateClickCountByLinkId(link.id, link.clickedCount.plus(1))
        }
    }

    fun incrementFolderClickCount(folder: Folder) {
        viewModelScope.launch {
            folderRepository.updateClickCountByFolderId(folder.id, folder.clickedCount.plus(1))
        }
    }
}
