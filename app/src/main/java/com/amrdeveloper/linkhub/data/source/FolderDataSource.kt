package com.amrdeveloper.linkhub.data.source

import androidx.paging.PagingSource
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.FolderSortingOption
import kotlinx.coroutines.flow.Flow

interface FolderDataSource {

    suspend fun insertFolder(folder: Folder): Result<Long>

    suspend fun insertFolders(folder: List<Folder>): Result<Unit>

    suspend fun getFolderById(id: Int): Result<Folder>

    suspend fun getFolderByName(name: String): Result<Folder>

    suspend fun getFolderList(): Result<List<Folder>>

    fun getMoseUsedFoldersWithPagination(): PagingSource<Int, Folder>

    fun getSortedFolders(
        keyword: String? = null,
        isPinned: Boolean? = null,
        isClicked: Boolean? = null,
        isInsideFolder: Boolean? = null,
        folderId: Int? = -1,
        sortingOption: FolderSortingOption = FolderSortingOption.DEFAULT,
        limit: Int = -1
    ): Flow<List<Folder>>

    suspend fun updateFolder(folder: Folder): Result<Int>

    suspend fun updateClickCountByFolderId(folderId: Int, count: Int): Result<Int>

    suspend fun deleteFolderWithLinks(folderId: Int): Result<Int>

    suspend fun deleteAll(): Result<Int>
}