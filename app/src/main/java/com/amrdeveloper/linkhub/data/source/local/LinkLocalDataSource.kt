package com.amrdeveloper.linkhub.data.source.local

import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.source.LinkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LinkLocalDataSource internal constructor(
    private val linkDao: LinkDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LinkDataSource {

    override suspend fun insertLink(link: Link): Result<Long> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.insert(link))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertLinks(link: List<Link>): Result<Unit> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.insertList(link))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLinkList(): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.getLinkList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPinnedLinkList(): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.getPinnedLinkList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSortedLinkList(): Flow<List<Link>> = linkDao.getSortedLinkList()

    override suspend fun getSortedFolderLinkList(id: Int): Result<List<Link>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(linkDao.getSortedLinkListByFolderId(id))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun getSortedFolderLinkListFlow(id: Int): Flow<List<Link>> =
        linkDao.getSortedLinkListByFolderIdFlow(id)

    override suspend fun getSortedLinkListByKeyword(keyword: String): Result<List<Link>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(linkDao.getSortedLinkListByKeyword(keyword))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getSortedFolderLinkListByKeyword(
        id: Int,
        keyword: String
    ): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.getSortedLinkListByKeywordByFolderId(id, keyword))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSortedFolderLinkListByKeywordFlow(
        id: Int,
        keyword: String
    ): Flow<List<Link>> = linkDao.getSortedLinkListByKeywordByFolderIdFlow(id, keyword)

    override suspend fun updateLink(link: Link): Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.update(link))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateClickCountByLinkId(linkId: Int, count: Int): Result<Int> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(linkDao.updateClickCountByLinkId(linkId, count))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun incrementClickCounter(linkId: Int): Result<Int> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(linkDao.incrementClickCounter(linkId))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteLink(link: Link): Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.delete(link))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteLinkByID(id: Int): Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.deleteLinkById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAll(): Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(linkDao.deleteAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}