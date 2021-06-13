package com.amrdeveloper.linkhub.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link")
data class Link (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") var title : String,
    @ColumnInfo(name = "subtitle") var subtitle : String,
    @ColumnInfo(name = "url") var url : String,
    @ColumnInfo(name = "pinned") var isPinned : Boolean = false,
    @ColumnInfo(name = "folder_id") var folder_id : Int = -1,
    @ColumnInfo(name = "click_count") var clicked_count : Int = 0
)