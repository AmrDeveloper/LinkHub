package com.amrdeveloper.linkhub.data.source.local

import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Result
import com.amrdeveloper.linkhub.data.source.FolderDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class FolderLocalDataSource internal constructor(
    private val folderDao: FolderDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FolderDataSource {

    override suspend fun insertFolder(folder: Folder) {
        withContext(ioDispatcher) {
            folderDao.insert(folder)
        }
    }

    override suspend fun getFolderList(): Result<List<Folder>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(folderDao.getFolderList())
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSortedFolderList(): Result<List<Folder>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(folderDao.getSortedFolderList())
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getLimitedSortedFolderList(limit: Int): Result<List<Folder>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(folderDao.getLimitedSortedFolderList(limit))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSortedFolderListByKeyword(keyword: String): Result<List<Folder>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(folderDao.getSortedFolderListByKeyword(keyword))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateFolder(folder: Folder) {
        withContext(ioDispatcher){
            folderDao.delete(folder)
        }
    }

    override suspend fun deleteFolder(folder: Folder) {
        withContext(ioDispatcher){
            folderDao.delete(folder)
        }
    }

    override suspend fun deleteFolderByID(id: Int) {
        withContext(ioDispatcher){
            folderDao.deleteById(id)
        }
    }

    override suspend fun deleteAll() {
        withContext(ioDispatcher){
            folderDao.deleteAll()
        }
    }

}