package com.amrdeveloper.linkhub.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "link", indices = [Index(value = ["title"], unique = true)])
data class Link (
    @ColumnInfo(name = "title") var title : String,
    @ColumnInfo(name = "subtitle") var subtitle : String,
    @ColumnInfo(name = "url") var url : String,
    @ColumnInfo(name = "pinned") var isPinned : Boolean = false,
    @ColumnInfo(name = "folder_id") var folder_id : Int = -1,
    @ColumnInfo(name = "click_count") var clicked_count : Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int  =0,
) : Parcelable {
    override fun toString(): String = title
}