package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result

interface LinkDataSource {

    suspend fun insertLink(link: Link)

    suspend fun getLinkList(): Result<List<Link>>

    suspend fun getSortedLinkList(): Result<List<Link>>

    suspend fun getSortedLinkListByKeyword(keyword: String): Result<List<Link>>

    suspend fun updateLink(link: Link)

    suspend fun deleteLink(link: Link)

    suspend fun deleteLinkByID(id: Int)

    suspend fun deleteFolderLinks(folderId: Int)

    suspend fun deleteAll()
}