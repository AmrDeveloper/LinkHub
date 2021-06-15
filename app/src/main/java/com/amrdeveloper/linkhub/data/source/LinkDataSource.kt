package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Result

interface LinkDataSource {

    suspend fun insertLink(link: Link) : Result<Long>

    suspend fun getLinkList(): Result<List<Link>>

    suspend fun getSortedLinkList(): Result<List<Link>>

    suspend fun getSortedFolderLinkList(id: Int):  Result<List<Link>>

    suspend fun getSortedLinkListByKeyword(keyword: String): Result<List<Link>>

    suspend fun getSortedFolderLinkListByKeyword(id: Int, keyword : String) :  Result<List<Link>>

    suspend fun updateLink(link: Link): Result<Int>

    suspend fun deleteLink(link: Link): Result<Int>

    suspend fun deleteLinkByID(id: Int): Result<Int>

    suspend fun deleteFolderLinks(folderId: Int): Result<Int>

    suspend fun deleteAll(): Result<Int>
}