package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listItems: List<T>)

    @Update
    suspend fun update(item: T): Int

    @Delete
    suspend fun delete(item: T): Int
}