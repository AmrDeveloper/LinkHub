package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Folder
import kotlinx.coroutines.flow.Flow

class FolderRepository(private val dataSource: FolderDataSource) {

    suspend fun insertFolder(folder: Folder): Result<Long> {
        return dataSource.insertFolder(folder)
    }

    suspend fun insertFolders(folders: List<Folder>): Result<Unit> {
        return dataSource.insertFolders(folders)
    }

    suspend fun getFolderByName(name: String): Result<Folder> {
        return dataSource.getFolderByName(name)
    }

    suspend fun getFolderList(): Result<List<Folder>> {
        return dataSource.getFolderList()
    }

    fun getSortedFolders(
        keyword: String? = null,
        isPinned: Boolean? = null,
        limit: Int = -1
    ): Flow<List<Folder>> = dataSource.getSortedFolders(keyword, isPinned, limit)

    suspend fun updateFolder(folder: Folder): Result<Int> {
        return dataSource.updateFolder(folder)
    }

    suspend fun updateClickCountByFolderId(folderId: Int, count: Int): Result<Int> {
        return dataSource.updateClickCountByFolderId(folderId, count)
    }

    suspend fun deleteFolderByID(id: Int): Result<Int> {
        return dataSource.deleteFolderWithLinks(id)
    }

    suspend fun deleteAll(): Result<Int> {
        return dataSource.deleteAll()
    }
}