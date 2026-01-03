package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Link
import kotlinx.coroutines.flow.Flow

class LinkRepository(private val dataSource: LinkDataSource) {

    suspend fun insertLink(link: Link): Result<Long> {
        return dataSource.insertLink(link)
    }

    suspend fun insertLinks(links: List<Link>): Result<Unit> {
        return dataSource.insertLinks(links)
    }

    suspend fun getLinkList(): Result<List<Link>> {
        return dataSource.getLinkList()
    }

    suspend fun getPinnedLinkList(): Result<List<Link>> {
        return dataSource.getPinnedLinkList()
    }

    suspend fun getSortedFolderLinkList(id: Int): Result<List<Link>> {
        return dataSource.getSortedFolderLinkList(id)
    }

    fun getSortedLinks(
        keyword: String? = null,
        isPinned: Boolean? = null,
        isClicked: Boolean? = null,
        isInsideFolder: Boolean? = null,
        folderId: Int? = null,
        limit: Int = -1
    ): Flow<List<Link>> =
        dataSource.getSortedLinks(keyword, isPinned, isClicked, isInsideFolder, folderId, limit)

    suspend fun updateLink(link: Link): Result<Int> {
        return dataSource.updateLink(link)
    }

    suspend fun updateClickCountByLinkId(linkId: Int, count: Int): Result<Int> {
        return dataSource.updateClickCountByLinkId(linkId, count)
    }

    suspend fun incrementClickCounter(linkId: Int): Result<Int> {
        return dataSource.incrementClickCounter(linkId)
    }

    suspend fun deleteLink(link: Link): Result<Int> {
        return dataSource.deleteLink(link)
    }

    suspend fun deleteAll(): Result<Int> {
        return dataSource.deleteAll()
    }
}