package com.amrdeveloper.linkhub.ui.link

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.common.TaskState
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.ui.folders.FolderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val linkRepository: LinkRepository,
) : ViewModel() {

    val selectSortedFoldersState: StateFlow<FolderUiState> =
        folderRepository.getSortedFolderListFlow()
            .map { FolderUiState(folders = it, isLoading = false) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = FolderUiState(isLoading = true)
            )

    var taskState by mutableStateOf<TaskState>(TaskState.Idle)
        private set

    fun createNewLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.insertLink(link)
            taskState = if (result.isSuccess) {
                if (result.getOrDefault(-1) > 0) TaskState.Success
                else TaskState.Error( R.string.error_link_same_title)
            } else {
                TaskState.Error( R.string.error_insert_link)
            }
        }
    }

    fun updateLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.updateLink(link)
            taskState = if (result.isSuccess && result.getOrDefault(-1) > 0) {
                TaskState.Success
            } else {
                TaskState.Error( R.string.error_update_link)
            }
        }
    }

    fun deleteLink(link: Link) {
        viewModelScope.launch {
            val result = linkRepository.deleteLink(link)
            taskState = if (result.isSuccess && result.getOrDefault(-1) > 0) {
                TaskState.Success
            } else {
                TaskState.Error( R.string.error_delete_link)
            }
        }
    }
}