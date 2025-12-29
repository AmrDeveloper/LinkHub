package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Folder
import kotlinx.coroutines.flow.Flow

interface FolderDataSource {

    suspend fun insertFolder(folder: Folder): Result<Long>

    suspend fun insertFolders(folder: List<Folder>): Result<Unit>

    suspend fun getFolderById(id: Int): Result<Folder>

    suspend fun getFolderByName(name: String): Result<Folder>

    suspend fun getFolderList(): Result<List<Folder>>

    fun getSortedFolderListFlow(): Flow<List<Folder>>

    suspend fun getSortedFolderList(): Result<List<Folder>>

    suspend fun getLimitedSortedFolderList(limit: Int): Result<List<Folder>>

    fun getSortedFolderListByKeyword(keyword: String): Flow<List<Folder>>

    suspend fun updateFolder(folder: Folder): Result<Int>

    suspend fun updateClickCountByFolderId(folderId: Int, count: Int): Result<Int>

    suspend fun deleteFolderWithLinks(folderId: Int): Result<Int>

    suspend fun deleteAll(): Result<Int>
}