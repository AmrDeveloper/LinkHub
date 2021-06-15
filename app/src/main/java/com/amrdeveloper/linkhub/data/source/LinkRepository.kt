package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result

class LinkRepository(private val dataSource: LinkDataSource) {

    suspend fun insertLink(link: Link): Result<Long> {
        return dataSource.insertLink(link)
    }

    suspend fun getLinkList(): Result<List<Link>> {
        return dataSource.getLinkList()
    }

    suspend fun getSortedLinkList(): Result<List<Link>> {
        return dataSource.getSortedLinkList()
    }

    suspend fun getSortedFolderLinkList(id: Int): Result<List<Link>> {
        return dataSource.getSortedFolderLinkList(id)
    }

    suspend fun getSortedLinkListByKeyword(keyword: String): Result<List<Link>> {
        return dataSource.getSortedLinkListByKeyword(keyword)
    }

    suspend fun getSortedFolderLinkListByKeyword(id: Int, keyword: String) : Result<List<Link>> {
        return dataSource.getSortedFolderLinkListByKeyword(id, keyword)
    }

    suspend fun updateLink(link: Link): Result<Int> {
        return dataSource.updateLink(link)
    }

    suspend fun deleteLink(link: Link): Result<Int> {
        return dataSource.deleteLink(link)
    }

    suspend fun deleteLinkByID(id: Int): Result<Int> {
        return dataSource.deleteLinkByID(id)
    }

    suspend fun deleteFolderLinks(folderId: Int): Result<Int> {
        return dataSource.deleteFolderLinks(folderId)
    }

    suspend fun deleteAll(): Result<Int> {
        return dataSource.deleteAll()
    }
}