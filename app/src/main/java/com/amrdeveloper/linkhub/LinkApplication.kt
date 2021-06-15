package com.amrdeveloper.linkhub

import android.app.Application
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.data.source.local.FolderLocalDataSource
import com.amrdeveloper.linkhub.data.source.local.LinkLocalDataSource
import com.amrdeveloper.linkhub.data.source.local.LinkRoomDatabase
import timber.log.Timber

class LinkApplication : Application() {

    private val database by lazy { LinkRoomDatabase.getDatabase(this) }

    private val folderDataSource by lazy { FolderLocalDataSource(database.folderDao()) }

    private val linkDataSource by lazy { LinkLocalDataSource(database.linkDao()) }

    val folderRepository by lazy { FolderRepository(folderDataSource) }

    val linkRepository by lazy { LinkRepository(linkDataSource) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}