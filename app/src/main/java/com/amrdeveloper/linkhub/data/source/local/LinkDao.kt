package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.amrdeveloper.linkhub.data.Link

@Dao
interface LinkDao : BaseDao<Link> {

    @Query("SELECT * FROM link")
    suspend fun getLinkList(): List<Link>

    @Query("SELECT * FROM link WHERE pinned = 1")
    suspend fun getPinnedLinkList() : List<Link>

    @Query("SELECT * FROM link ORDER BY pinned DESC, click_count DESC")
    suspend fun getSortedLinkList(): List<Link>

    @Query("SELECT * FROM link WHERE folder_id = :id ORDER BY pinned  DESC, click_count DESC")
    suspend fun getSortedLinkListByFolderId(id: Int): List<Link>

    @Query("SELECT * FROM link WHERE title LIKE '%' || :keyword || '%' ORDER BY pinned  DESC, click_count DESC")
    suspend fun getSortedLinkListByKeyword(keyword : String) : List<Link>

    @Query("SELECT * FROM link WHERE folder_id = :id AND title LIKE '%' || :keyword || '%' ORDER BY pinned  DESC, click_count DESC")
    suspend fun getSortedLinkListByKeywordByFolderId(id: Int, keyword : String) : List<Link>

    @Query("UPDATE link SET click_count = :count WHERE id = :linkId")
    suspend fun updateClickCountByLinkId(linkId : Int, count : Int) : Int

    @Query("UPDATE link SET click_count = click_count + 1 WHERE id = :linkId")
    suspend fun incrementClickCounter(linkId : Int) : Int

    @Query("DELETE FROM link WHERE id = :id")
    suspend fun deleteLinkById(id: Int): Int

    @Query("DELETE FROM link")
    suspend fun deleteAll(): Int
}