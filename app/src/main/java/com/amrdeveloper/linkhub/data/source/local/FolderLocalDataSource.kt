package com.amrdeveloper.linkhub.data.source.local

import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.source.FolderDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FolderLocalDataSource internal constructor(
    private val folderDao: FolderDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FolderDataSource {

    override suspend fun insertFolder(folder: Folder): Result<Long> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(folderDao.insert(folder))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertFolders(folder: List<Folder>): Result<Unit> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(folderDao.insertList(folder))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getFolderById(id: Int): Result<Folder> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(folderDao.getFolderById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFolderByName(name: String): Result<Folder> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(folderDao.getFolderByName(name))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFolderList(): Result<List<Folder>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(folderDao.getFolders())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSortedFolders(
        keyword: String?,
        isPinned: Boolean?,
        isClicked: Boolean?,
        isInsideFolder: Boolean?,
        folderId: Int?,
        limit: Int
    ): Flow<List<Folder>> =
        folderDao.getSortedFolders(keyword, isPinned, isClicked, isInsideFolder, folderId, limit)

    override suspend fun updateFolder(folder: Folder): Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(folderDao.update(folder))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateClickCountByFolderId(folderId: Int, count: Int): Result<Int> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(folderDao.updateClickCountByFolderId(folderId, count))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteFolderWithLinks(folderId: Int): Result<Int> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(folderDao.deleteFolderWithLinks(folderId))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteAll(): Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(folderDao.deleteAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}