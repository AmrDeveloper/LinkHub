package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.amrdeveloper.linkhub.data.Folder

@Dao
interface FolderDao : BaseDao<Folder> {

    @Query("SELECT * FROM folder")
    suspend fun getFolderList(): List<Folder>

    @Query("SELECT * FROM folder ORDER BY pinned, click_count ASC")
    suspend fun getSortedFolderList(): List<Folder>

    @Query("SELECT * FROM folder ORDER BY pinned, click_count ASC LIMIT :limit")
    suspend fun getLimitedSortedFolderList(limit: Int): List<Folder>

    @Query("SELECT * FROM folder WHERE name LIKE '%' || :keyword || '%' ORDER BY pinned, click_count ASC")
    suspend fun getSortedFolderListByKeyword(keyword : String) : List<Folder>

    @Query("DELETE FROM folder WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("DELETE FROM folder")
    suspend fun deleteAll(): Int
}