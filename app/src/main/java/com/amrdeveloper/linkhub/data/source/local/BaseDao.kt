package com.amrdeveloper.linkhub.data.source.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.amrdeveloper.linkhub.data.Link

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item : Link) : Long

    @Update
    suspend fun update(item: T) : Int

    @Delete
    suspend fun delete(item: T) : Int
}