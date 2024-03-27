package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.amrdeveloper.linkhub.data.Folder

@Dao
interface FolderDao : BaseDao<Folder> {

    @Query("SELECT * FROM folder WHERE id = :id LIMIT 1")
    suspend fun getFolderById(id : Int) : Folder

    @Query("SELECT * FROM folder WHERE name = :name LIMIT 1")
    suspend fun getFolderByName(name : String) : Folder

    @Query("SELECT * FROM folder")
    suspend fun getFolderList(): List<Folder>

    @Query("SELECT * FROM folder ORDER BY pinned DESC, click_count DESC")
    suspend fun getSortedFolderList(): List<Folder>

    @Query("SELECT * FROM folder ORDER BY pinned DESC, click_count DESC LIMIT :limit")
    suspend fun getLimitedSortedFolderList(limit: Int): List<Folder>

    @Query("SELECT * FROM folder WHERE name LIKE '%' || :keyword || '%' ORDER BY pinned DESC, click_count DESC")
    suspend fun getSortedFolderListByKeyword(keyword : String) : List<Folder>

    @Query("UPDATE folder SET click_count = :count WHERE id = :folderId")
    suspend fun updateClickCountByFolderId(folderId : Int, count : Int) : Int

    @Query("DELETE FROM folder WHERE id = :id")
    suspend fun deleteFolderById(id: Int): Int

    @Query("DELETE FROM link WHERE folder_id = :folderId")
    suspend fun deleteFolderLinks(folderId: Int): Int

    @Query("DELETE FROM folder")
    suspend fun deleteAll(): Int

    @Transaction
    suspend fun deleteFolderWithLinks(folderId : Int) : Int {
        val deleteResult = deleteFolderById(folderId)
        deleteFolderLinks(folderId)
        return deleteResult
    }
}