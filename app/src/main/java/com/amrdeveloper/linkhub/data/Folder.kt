package com.amrdeveloper.linkhub.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "folder", indices = [Index(value = ["name"], unique = true)])
data class Folder(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "pinned") var isPinned: Boolean = false,
    @ColumnInfo(name = "click_count") var clickedCount: Int = 0,
    @ColumnInfo(name = "color_name", defaultValue = "BLUE") var folderColor: FolderColor = FolderColor.BLUE,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : Parcelable {
    override fun toString(): String = name
}