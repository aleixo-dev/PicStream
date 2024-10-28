package com.nicolas.picstream.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nicolas.picstream.data.local.dao.NotificationDao
import com.nicolas.picstream.data.local.dao.PhotoDao
import com.nicolas.picstream.data.local.entity.NotificationEntity
import com.nicolas.picstream.data.local.entity.PhotoEntity

@Database(
    entities = [
        PhotoEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun notificationDao(): NotificationDao

}