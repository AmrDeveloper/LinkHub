package com.amrdeveloper.linkhub.ui.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.common.LazyValue
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.FolderSortingOption
import com.amrdeveloper.linkhub.data.source.FolderRepository
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

@HiltViewModel
class FolderListViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val sortingOption = MutableStateFlow(value = FolderSortingOption.DEFAULT)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<LazyValue<List<Folder>>> =
        combine(sortingOption) {
            sortingOption.value
        }.flatMapLatest { option ->
            folderRepository.getSortedFolders(sortingOption = option)
        }.map { LazyValue(data = it, isLoading = false) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LazyValue(data = listOf(), isLoading = true)
        )

    fun setSortingOption(option: FolderSortingOption) {
        sortingOption.value = option
    }

    fun incrementFolderClickCount(folder: Folder) {
        viewModelScope.launch {
            folderRepository.updateClickCountByFolderId(folder.id, folder.clickedCount.plus(1))
        }
    }
}