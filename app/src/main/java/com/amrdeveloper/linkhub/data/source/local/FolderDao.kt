package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amrdeveloper.linkhub.data.Folder

@Dao
interface FolderDao : BaseDao<Folder> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item : Folder) : Long

    @Query("SELECT * FROM folder WHERE id = :id LIMIT 1")
    suspend fun getFolderById(id : Int) : Folder

    @Query("SELECT * FROM folder")
    suspend fun getFolderList(): List<Folder>

    @Query("SELECT * FROM folder ORDER BY pinned DESC, click_count DESC")
    suspend fun getSortedFolderList(): List<Folder>

    @Query("SELECT * FROM folder ORDER BY pinned DESC, click_count DESC LIMIT :limit")
    suspend fun getLimitedSortedFolderList(limit: Int): List<Folder>

    @Query("SELECT * FROM folder WHERE name LIKE '%' || :keyword || '%' ORDER BY pinned DESC, click_count DESC")
    suspend fun getSortedFolderListByKeyword(keyword : String) : List<Folder>

    @Query("DELETE FROM folder WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("DELETE FROM folder")
    suspend fun deleteAll(): Int
}