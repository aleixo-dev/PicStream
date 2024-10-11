package com.nicolas.picstream.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nicolas.picstream.data.local.entity.PhotoEntity

@Dao
interface PhotoDao {

    @Upsert
    suspend fun insert(photoEntity: List<PhotoEntity>)

    @Query("SELECT * FROM photoentity")
    fun pagingSource(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM photoentity")
    suspend fun clearAll()

}