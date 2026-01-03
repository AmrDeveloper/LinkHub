package com.amrdeveloper.linkhub.data.source.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link

const val DATABASE_NAME = "link_database"
const val DATABASE_VERSION = 6

@Database(
    entities = [Link::class, Folder::class],
    version = DATABASE_VERSION,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6)
    ],
    exportSchema = true,
)
@TypeConverters(FolderColorConverter::class)
abstract class LinkRoomDatabase : RoomDatabase() {

    abstract fun linkDao(): LinkDao

    abstract fun folderDao(): FolderDao

    companion object {

        @Volatile
        private var INSTANCE: LinkRoomDatabase? = null

        fun getDatabase(context: Context): LinkRoomDatabase {
            val MIGRATION_5_6 = object : Migration(startVersion = 5, endVersion = 6) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        """
                              ALTER TABLE folder
                              ADD COLUMN folder_id INTEGER NOT NULL DEFAULT -1
                              """
                    )
                }
            }

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    klass = LinkRoomDatabase::class.java, DATABASE_NAME
                )
                .addMigrations(MIGRATION_5_6)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}