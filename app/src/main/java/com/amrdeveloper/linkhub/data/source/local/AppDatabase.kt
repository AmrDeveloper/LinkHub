package com.amrdeveloper.linkhub.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link

const val DATABASE_NAME = "link_database"

@Database(entities = [Link::class, Folder::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun linkDao(): LinkDao

    abstract fun folderDao(): FolderDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}