package com.amrdeveloper.linkhub.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.amrdeveloper.linkhub.data.Folder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FolderDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: LinkRoomDatabase
    private lateinit var folderDao: FolderDao

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LinkRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        folderDao = database.folderDao()
    }

    @Test
    fun insert_uniqueFolder() = runBlockingTest {
        val folder = Folder("Word")
        val insertResult = folderDao.insert(folder)
        assertThat(insertResult).isGreaterThan(-1)
    }

    @Test
    fun insert_unUniqueFolder() = runBlockingTest  {
        val folder = Folder("Word")
        folderDao.insert(folder)
        val folder2 = Folder("Word")
        val insertResult = folderDao.insert(folder2)
        assertThat(insertResult).isEqualTo(-1)
    }

    @Test
    fun read_sortedFolderListByClickCount() = runBlockingTest {
        val folder = Folder("Word", id = 1, clickedCount = 1)
        folderDao.insert(folder)

        val folder2 = Folder("Jobs", id = 2, clickedCount = 5)
        folderDao.insert(folder2)

        val sortedList = folderDao.getSortedFolderList()
        assertThat(sortedList.first()).isEqualTo(folder2)
    }

    @Test
    fun read_sortedFolderListByPinned() = runBlockingTest {
        val folder = Folder("Word", id = 1, isPinned = false)
        folderDao.insert(folder)

        val folder2 = Folder("Jobs", id = 2, isPinned = true)
        folderDao.insert(folder2)

        val sortedList = folderDao.getSortedFolderList()
        assertThat(sortedList.first()).isEqualTo(folder2)
    }

    @Test
    fun read_sortedFolderListByPinnedFirst() = runBlockingTest {
        val folder = Folder("Word", id = 1, clickedCount = 10, isPinned = false)
        folderDao.insert(folder)

        val folder2 = Folder("Jobs", id = 2, clickedCount = 5, isPinned = true)
        folderDao.insert(folder2)

        val sortedList = folderDao.getSortedFolderList()
        assertThat(sortedList.first()).isEqualTo(folder2)
    }

    @Test
    fun update_folderClickCount() = runBlockingTest {
        val folder = Folder("Word")
        folderDao.insert(folder)

        val folderID = 1
        folderDao.updateClickCountByFolderId(folderID, 1)
        val folder2 = folderDao.getFolderById(folderID)

        assertThat(folder2.clickedCount).isEqualTo(1)
    }

    @After
    fun closeDatabase() {
        database.close()
    }
}