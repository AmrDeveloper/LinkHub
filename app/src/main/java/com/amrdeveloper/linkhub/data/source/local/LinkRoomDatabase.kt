package com.amrdeveloper.linkhub.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link

const val DATABASE_NAME = "link_database"

@Database(entities = [Link::class, Folder::class], version = 1)
abstract class LinkRoomDatabase : RoomDatabase() {

    abstract fun linkDao(): LinkDao

    abstract fun folderDao(): FolderDao

    companion object {

        @Volatile
        private var INSTANCE: LinkRoomDatabase? = null

        fun getDatabase(context: Context): LinkRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LinkRoomDatabase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}