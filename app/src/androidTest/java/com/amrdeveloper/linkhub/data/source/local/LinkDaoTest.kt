package com.amrdeveloper.linkhub.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.amrdeveloper.linkhub.data.Link
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LinkDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: LinkRoomDatabase
    private lateinit var linkDao: LinkDao

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LinkRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        linkDao = database.linkDao()
    }

    @Test
    fun insert_uniqueLink() = runBlockingTest {
        val link = Link("title", "subtitle", "url")
        val insertResult = linkDao.insert(link)
        assertThat(insertResult).isGreaterThan(-1)
    }

    @Test
    fun insert_unUniqueLink() = runBlockingTest {
        val link = Link("title", "subtitle", "url")
        linkDao.insert(link)
        val insertResult = linkDao.insert(link)
        assertThat(insertResult).isEqualTo(-1)
    }

    @Test
    fun read_pinnedLinkList() = runBlockingTest {
        val link = Link("title", "subtitle", "url", id = 1, isPinned = false)
        val link2 = Link("title2", "subtitle2", "url2", id = 2, isPinned = true)

        linkDao.insert(link)
        linkDao.insert(link2)

        val pinnedList = linkDao.getPinnedLinkList()

        assertThat(pinnedList.size).isEqualTo(1)
        assertThat(pinnedList[0]).isEqualTo(link2)
    }

    @Test
    fun read_sortedLinkListByClickCount() = runBlockingTest {
        val link = Link("title", "subtitle", "url", id = 1, clickedCount = 5)
        linkDao.insert(link)

        val link2 = Link("title2", "subtitle2", "url2", id = 2, clickedCount = 10)
        linkDao.insert(link2)

        val sortedList = linkDao.getSortedLinkList()
        assertThat(sortedList.first()).isEqualTo(link2)
    }

    @Test
    fun read_sortedLinkListByPinned() = runBlockingTest {
        val link = Link("title", "subtitle", "url", id = 1)
        linkDao.insert(link)

        val link2 = Link("title2", "subtitle2", "url2", id = 2, isPinned = true)
        linkDao.insert(link2)

        val sortedList = linkDao.getSortedLinkList()
        assertThat(sortedList.first()).isEqualTo(link2)
    }

    @Test
    fun read_sortedLinkListByPinnedFirst() = runBlockingTest {
        val link = Link("title", "subtitle", "url", id = 1, clickedCount = 10)
        linkDao.insert(link)

        val link2 = Link("title2", "subtitle2", "url2", id = 2, clickedCount = 5, isPinned = true)
        linkDao.insert(link2)

        val sortedList = linkDao.getSortedLinkList()
        assertThat(sortedList.first()).isEqualTo(link2)
    }

    @After
    fun closeDatabase() {
        database.close()
    }
}