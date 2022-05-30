package com.amrdeveloper.linkhub.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "link", indices = [Index(value = ["title"], unique = true)])
data class Link (
    @ColumnInfo(name = "title") var title : String,
    @ColumnInfo(name = "subtitle") var subtitle : String,
    @ColumnInfo(name = "url") var url : String,
    @ColumnInfo(name = "pinned") var isPinned : Boolean = false,
    @ColumnInfo(name = "folder_id") var folderId : Int = -1,
    @ColumnInfo(name = "click_count") var clickedCount : Int = 0,
    @ColumnInfo(name = "time_stamp", defaultValue = "0") var timeStamp : Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_updated", defaultValue = "0") var isUpdated : Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int  =0,
) : Parcelable {
    override fun toString(): String = title
}