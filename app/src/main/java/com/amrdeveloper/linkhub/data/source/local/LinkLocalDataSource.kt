package com.amrdeveloper.linkhub.data.source.local

import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result
import com.amrdeveloper.linkhub.data.source.LinkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LinkLocalDataSource internal constructor(
    private val linkDao: LinkDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LinkDataSource {

    override suspend fun insertLink(link: Link) : Result<Long> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.insert(link))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getLinkList(): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.getLinkList())
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSortedLinkList(): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.getSortedLinkList())
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSortedFolderLinkList(id: Int): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.getSortedLinkListByFolderId(id))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSortedLinkListByKeyword(keyword: String): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.getSortedLinkListByKeyword(keyword))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSortedFolderLinkListByKeyword(id: Int, keyword: String): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.getSortedLinkListByKeywordByFolderId(id, keyword))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateLink(link: Link) : Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.update(link))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteLink(link: Link) : Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.delete(link))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteLinkByID(id: Int) : Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.deleteLinkById(id))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteFolderLinks(folderId: Int) : Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.deleteFolderLinks(folderId))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteAll() : Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.deleteAll())
        } catch (e : Exception) {
            Result.Error(e)
        }
    }

}