package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.amrdeveloper.linkhub.data.Link

@Dao
interface LinkDao : BaseDao<Link> {

    @Query("SELECT * FROM link")
    suspend fun getLinkList(): List<Link>

    @Query("SELECT * FROM link ORDER BY pinned DESC, click_count ASC")
    suspend fun getSortedLinkList(): List<Link>

    @Query("SELECT * FROM link WHERE folder_id = :id ORDER BY pinned  DESC, click_count ASC")
    suspend fun getSortedLinkListByFolderId(id: Int): List<Link>

    @Query("SELECT * FROM link WHERE title LIKE '%' || :keyword || '%' ORDER BY pinned  DESC, click_count ASC")
    suspend fun getSortedLinkListByKeyword(keyword : String) : List<Link>

    @Query("SELECT * FROM link WHERE folder_id = :id AND title LIKE '%' || :keyword || '%' ORDER BY pinned  DESC, click_count ASC")
    suspend fun getSortedLinkListByKeywordByFolderId(id: Int, keyword : String) : List<Link>

    @Query("DELETE FROM link WHERE id = :id")
    suspend fun deleteLinkById(id: Int): Int

    @Query("DELETE FROM link WHERE folder_id = :folderId")
    suspend fun deleteFolderLinks(folderId: Int): Int

    @Query("DELETE FROM link")
    suspend fun deleteAll(): Int
}