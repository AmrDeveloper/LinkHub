package com.amrdeveloper.linkhub.data.source.local

import android.content.Context
import androidx.room.*
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link

const val DATABASE_NAME = "link_database"
const val DATABASE_VERSION = 5

@Database(
    entities = [Link::class, Folder::class],
    version = DATABASE_VERSION,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5)
    ]
)
@TypeConverters(FolderColorConverter::class)
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