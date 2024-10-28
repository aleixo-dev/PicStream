package com.nicolas.picstream.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nicolas.picstream.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadNotification(notificationEntity: NotificationEntity) : Long

    @Query("SELECT * FROM notificationentity ORDER BY date DESC")
    fun selectAllNotification() : Flow<List<NotificationEntity>>

    @Query("DELETE FROM notificationentity")
    suspend fun deleteAll()

}