package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Folder

class FolderRepository(private val dataSource: FolderDataSource) {

    suspend fun insertFolder(folder: Folder): Result<Long> {
        return dataSource.insertFolder(folder)
    }

    suspend fun insertFolders(folders: List<Folder>): Result<Unit> {
        return dataSource.insertFolders(folders)
    }

    suspend fun getFolderById(folderId : Int) : Result<Folder> {
        return dataSource.getFolderById(folderId)
    }
    suspend fun getFolderByName(name : String) : Result<Folder> {
        return dataSource.getFolderByName(name)
    }

    suspend fun getFolderList(): Result<List<Folder>> {
        return dataSource.getFolderList()
    }

    suspend fun getSortedFolderList(): Result<List<Folder>> {
        return dataSource.getSortedFolderList()
    }

    suspend fun getLimitedSortedFolderList(limit: Int): Result<List<Folder>> {
        return dataSource.getLimitedSortedFolderList(limit)
    }

    suspend fun getSortedFolderListByKeyword(keyword: String): Result<List<Folder>> {
        return dataSource.getSortedFolderListByKeyword(keyword)
    }

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