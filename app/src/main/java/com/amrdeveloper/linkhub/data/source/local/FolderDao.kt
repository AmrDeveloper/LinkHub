package com.amrdeveloper.linkhub.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.FolderSortingOption
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao : BaseDao<Folder> {

    @Query("SELECT * FROM folder WHERE id = :id LIMIT 1")
    suspend fun getFolderById(id: Int): Folder

    @Query("SELECT * FROM folder WHERE name = :name LIMIT 1")
    suspend fun getFolderByName(name: String): Folder

    @Query("SELECT * FROM folder")
    suspend fun getFolders(): List<Folder>

    @Query(
        """
        SELECT * FROM folder
        ORDER BY pinned DESC, click_count DESC
    """
    )
    fun getMoseUsedFoldersWithPagination(): PagingSource<Int, Folder>

    @Query(
        """
        SELECT * FROM folder
        WHERE ((:keyword IS NULL) OR (:keyword = '') OR (name LIKE '%' || :keyword || '%'))
        AND   ((:isPinned IS NULL) OR (pinned = :isPinned))
        AND   ((:isClicked IS NULL) OR (click_count > 0))
        AND   ((:isInsideFolder IS NULL) OR (folder_id != -1))
        AND   ((:folderId IS NULL) OR ((:folderId = -1)) OR (folder_id = :folderId))
        ORDER BY 
            CASE WHEN :sortingOption = 'NAME_ASC' THEN name END ASC,
            CASE WHEN :sortingOption = 'NAME_DESC' THEN name END DESC,
            CASE WHEN :sortingOption = 'CLICK_COUNT_ASC' THEN click_count END ASC,
            CASE WHEN :sortingOption = 'CLICK_COUNT_DESC' THEN click_count END DESC,
            CASE WHEN :sortingOption = 'DEFAULT' THEN pinned END DESC,
            CASE WHEN :sortingOption = 'DEFAULT' THEN click_count END DESC
        LIMIT :limit
    """
    )
    fun getSortedFolders(
        keyword: String? = null,
        isPinned: Boolean? = null,
        isClicked: Boolean? = null,
        isInsideFolder: Boolean? = null,
        folderId: Int? = null,
        sortingOption: FolderSortingOption = FolderSortingOption.DEFAULT,
        limit: Int = -1
    ): Flow<List<Folder>>

    @Query("UPDATE folder SET click_count = :count WHERE id = :folderId")
    suspend fun updateClickCountByFolderId(folderId: Int, count: Int): Int

    @Query("DELETE FROM folder WHERE id = :id")
    suspend fun deleteFolderById(id: Int): Int

    @Query("DELETE FROM link WHERE folder_id = :folderId")
    suspend fun deleteFolderLinks(folderId: Int): Int

    @Query("DELETE FROM folder")
    suspend fun deleteAll(): Int

    @Transaction
    suspend fun deleteFolderWithLinks(folderId: Int): Int {
        val deleteResult = deleteFolderById(folderId)
        deleteFolderLinks(folderId)
        return deleteResult
    }
}