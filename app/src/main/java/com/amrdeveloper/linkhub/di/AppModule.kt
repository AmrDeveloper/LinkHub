package com.amrdeveloper.linkhub.di

import android.content.Context
import com.amrdeveloper.linkhub.data.source.FolderDataSource
import com.amrdeveloper.linkhub.data.source.FolderRepository
import com.amrdeveloper.linkhub.data.source.LinkDataSource
import com.amrdeveloper.linkhub.data.source.LinkRepository
import com.amrdeveloper.linkhub.data.source.local.FolderLocalDataSource
import com.amrdeveloper.linkhub.data.source.local.LinkLocalDataSource
import com.amrdeveloper.linkhub.data.source.local.LinkRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LinkRoomDatabase {
        return LinkRoomDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideLinkLocalDataSource(
        database: LinkRoomDatabase,
        ioDispatcher: CoroutineDispatcher
    ): LinkDataSource {
        return LinkLocalDataSource(database.linkDao(), ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideFolderLocalDataSource(
        database: LinkRoomDatabase,
        ioDispatcher: CoroutineDispatcher
    ): FolderDataSource {
        return FolderLocalDataSource(database.folderDao(), ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideLinkRepository(linkDataSource: LinkDataSource): LinkRepository {
        return LinkRepository(linkDataSource)
    }

    @Singleton
    @Provides
    fun provideFolderRepository(folderDataSource: FolderDataSource): FolderRepository {
        return FolderRepository(folderDataSource)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}