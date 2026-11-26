package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.SortingOptions
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao : BaseDao<Link> {

    @Query("SELECT * FROM link")
    suspend fun getLinkList(): List<Link>

    @Query("SELECT * FROM link WHERE pinned = 1")
    suspend fun getPinnedLinkList(): List<Link>

    @Query("SELECT * FROM link WHERE folder_id = :id ORDER BY pinned  DESC, click_count DESC")
    suspend fun getSortedLinkListByFolderId(id: Int): List<Link>

    @Query(
        """
        SELECT * FROM link
        WHERE   ((:keyword IS NULL) OR (:keyword = '')
                OR (title LIKE '%' || :keyword || '%') 
                OR (subtitle LIKE '%' || :keyword || '%') 
                OR (url LIKE '%' || :keyword || '%'))
        AND   ((:isPinned IS NULL) OR (pinned = :isPinned))
        AND   ((:isClicked IS NULL) OR (click_count > 0))
        AND   ((:isInsideFolder IS NULL) OR (folder_id != -1))
        AND   ((:folderId IS NULL) OR ((:folderId = -1)) OR (folder_id = :folderId))
        ORDER BY 
            CASE WHEN :sortingOption = 'NAME_ASC' THEN title END ASC,
            CASE WHEN :sortingOption = 'NAME_DESC' THEN title END DESC,
            CASE WHEN :sortingOption = 'CLICK_COUNT_ASC' THEN click_count END ASC,
            CASE WHEN :sortingOption = 'CLICK_COUNT_DESC' THEN click_count END DESC,
            CASE WHEN :sortingOption = 'DEFAULT' THEN pinned END DESC,
            CASE WHEN :sortingOption = 'DEFAULT' THEN click_count END DESC
        LIMIT :limit
    """
    )
    fun getSortedLinks(
        keyword: String? = null,
        isPinned: Boolean? = null,
        isClicked: Boolean? = null,
        isInsideFolder: Boolean? = null,
        folderId: Int? = null,
        sortingOption: SortingOptions = SortingOptions.DEFAULT,
        limit: Int = -1
    ): Flow<List<Link>>

    @Query("UPDATE link SET click_count = :count WHERE id = :linkId")
    suspend fun updateClickCountByLinkId(linkId: Int, count: Int): Int

    @Query("UPDATE link SET click_count = click_count + 1 WHERE id = :linkId")
    suspend fun incrementClickCounter(linkId: Int): Int

    @Query("DELETE FROM link WHERE id = :id")
    suspend fun deleteLinkById(id: Int): Int

    @Query("DELETE FROM link")
    suspend fun deleteAll(): Int
}
