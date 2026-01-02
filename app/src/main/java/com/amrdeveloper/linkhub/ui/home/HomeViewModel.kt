package com.amrdeveloper.linkhub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.common.LazyValue
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    val mostUsedLimitedFoldersState: StateFlow<LazyValue<List<Folder>>> =
        folderRepository.getSortedFolders(limit = NUMBER_OF_TOP_FOLDERS)
            .map { LazyValue(data = it, isLoading = false) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = LazyValue(data = listOf(), isLoading = true)
            )

    val sortedLinksState: StateFlow<LazyValue<List<Link>>> =
        linkRepository.getSortedLinks()
            .map { LazyValue(data = it, isLoading = false) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = LazyValue(data = listOf(), isLoading = true)
            )

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