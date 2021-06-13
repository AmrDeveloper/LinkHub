package com.amrdeveloper.linkhub.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "folder")
data class Folder (
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "pinned") var isPinned : Boolean = false,
    @ColumnInfo(name = "click_count") var clickedCount : Int = 0,
) : Parcelable