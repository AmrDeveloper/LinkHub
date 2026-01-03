package com.amrdeveloper.linkhub.ui.folder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.common.LazyValue
import com.amrdeveloper.linkhub.common.TaskState
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.source.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel() {

    val selectSortedFoldersState: StateFlow<LazyValue<List<Folder>>> =
        folderRepository.getSortedFolders()
            .map { LazyValue(data = it, isLoading = false) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = LazyValue(data = listOf(), isLoading = true)
            )

    var taskState by mutableStateOf<TaskState>(TaskState.Idle)
        private set

    fun createNewFolder(folder: Folder) {
        viewModelScope.launch {
            val result = folderRepository.insertFolder(folder)
            taskState = if (result.isSuccess) {
                if (result.getOrDefault(-1) > 0) TaskState.Success
                else TaskState.Error(R.string.error_folder_same_name)
            } else {
                TaskState.Error(R.string.error_insert_folder)
            }
        }
    }

    fun updateFolder(folder: Folder) {
        viewModelScope.launch {
            val result = folderRepository.updateFolder(folder)
            taskState = if (result.isSuccess && result.getOrDefault(-1) > 0) {
                TaskState.Success
            } else {
                TaskState.Error(R.string.error_update_folder)
            }
        }
    }

    fun deleteFolder(folderId: Int) {
        viewModelScope.launch {
            val result = folderRepository.deleteFolderByID(folderId)
            taskState = if (result.isSuccess && result.getOrDefault(-1) > 0) {
                TaskState.Success
            } else {
                TaskState.Error(R.string.error_delete_folder)
            }
        }
    }
}