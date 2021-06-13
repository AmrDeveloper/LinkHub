package com.amrdeveloper.linkhub.data.source.local

import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result
import com.amrdeveloper.linkhub.data.source.LinkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LinkLocalDataSource internal constructor(
    private val linkDao: LinkDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LinkDataSource {

    override suspend fun insertLink(link: Link) {
        withContext(ioDispatcher) {
            linkDao.insert(link)
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

    override suspend fun getSortedLinkListByKeyword(keyword: String): Result<List<Link>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(linkDao.getSortedLinkListByKeyword(keyword))
        } catch (e : Exception) {
            Result.Error(e)
        }
    }
    override suspend fun updateLink(link: Link) {
        withContext(ioDispatcher){
            linkDao.update(link)
        }
    }

    override suspend fun deleteLink(link: Link) {
        withContext(ioDispatcher){
            linkDao.delete(link)
        }
    }

    override suspend fun deleteLinkByID(id: Int) {
        withContext(ioDispatcher){
            linkDao.deleteLinkById(id)
        }
    }

    override suspend fun deleteFolderLinks(folderId: Int) {
        withContext(ioDispatcher){
            linkDao.deleteFolderLinks(folderId)
        }
    }

    override suspend fun deleteAll() {
        withContext(ioDispatcher){
            linkDao.deleteAll()
        }
    }

}