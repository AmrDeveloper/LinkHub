package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Result

interface FolderDataSource {

    suspend fun insertFolder(folder: Folder)

    suspend fun getFolderList(): Result<List<Folder>>

    suspend fun getSortedFolderList(): Result<List<Folder>>

    suspend fun getLimitedSortedFolderList(limit: Int): Result<List<Folder>>

    suspend fun getSortedFolderListByKeyword(keyword: String): Result<List<Folder>>

    suspend fun updateFolder(folder: Folder)

    suspend fun deleteFolder(folder: Folder)

    suspend fun deleteFolderByID(id: Int)

    suspend fun deleteAll()
}