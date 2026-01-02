package com.amrdeveloper.linkhub.data.source

import com.amrdeveloper.linkhub.data.Link
import kotlinx.coroutines.flow.Flow

interface LinkDataSource {

    suspend fun insertLink(link: Link): Result<Long>

    suspend fun insertLinks(link: List<Link>): Result<Unit>

    suspend fun getLinkList(): Result<List<Link>>

    suspend fun getPinnedLinkList(): Result<List<Link>>

    suspend fun getSortedFolderLinkList(id: Int): Result<List<Link>>

    fun getSortedLinks(
        keyword: String? = null,
        isPinned: Boolean? = null,
        isClicked: Boolean? = null,
        folderId: Int? = null,
        limit: Int = -1
    ): Flow<List<Link>>

    suspend fun updateLink(link: Link): Result<Int>

    suspend fun updateClickCountByLinkId(linkId: Int, count: Int): Result<Int>

    suspend fun incrementClickCounter(linkId: Int): Result<Int>

    suspend fun deleteLink(link: Link): Result<Int>

    suspend fun deleteLinkByID(id: Int): Result<Int>

    suspend fun deleteAll(): Result<Int>
}