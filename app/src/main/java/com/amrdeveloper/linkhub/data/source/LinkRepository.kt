package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result

class LinkRepository(private val dataSource: LinkDataSource) {

    suspend fun insertLink(link: Link) {
        dataSource.insertLink(link)
    }

    suspend fun getLinkList() : Result<List<Link>> {
        return dataSource.getLinkList()
    }

    suspend fun getSortedLinkList(): Result<List<Link>> {
        return dataSource.getSortedLinkList()
    }

    suspend fun getSortedLinkListByKeyword(keyword : String) : Result<List<Link>> {
        return dataSource.getSortedLinkListByKeyword(keyword)
    }

    suspend fun updateLink(link: Link) {
        dataSource.updateLink(link)
    }

    suspend fun deleteLink(link: Link) {
        dataSource.deleteLink(link)
    }

    suspend fun deleteLinkByID(id : Int) {
        dataSource.deleteLinkByID(id)
    }

    suspend fun deleteFolderLinks(folderId : Int) {
        dataSource.deleteFolderLinks(folderId)
    }

    suspend fun deleteAll() {
        dataSource.deleteAll()
    }
}