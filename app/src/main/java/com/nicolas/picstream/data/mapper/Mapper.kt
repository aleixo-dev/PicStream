package com.nicolas.picstream.data.mapper

import com.nicolas.picstream.data.local.entity.NotificationEntity
import com.nicolas.picstream.data.local.entity.PhotoEntity
import com.nicolas.picstream.data.model.Notification
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.model.PhotoUrl
import com.nicolas.picstream.data.model.Topic
import com.nicolas.picstream.data.response.PagingPhotoResponse
import com.nicolas.picstream.data.response.PhotoResponse
import com.nicolas.picstream.data.response.TopicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun PhotoResponse.toDomain() = withContext(Dispatchers.IO) {
    Photo(
        id = id,
        url = src.original ?: "",
        photographerName = photographer ?: "",
        alt = alt ?: ""
    )
}

fun TopicResponse.toTopic() = Topic(
    id = id,
    slug = slug,
    title = title,
    status = status
)

suspend fun List<PagingPhotoResponse>.toDomain() =
    map { it.photos.map { photos -> photos.toDomain() } }


fun PhotoResponse.toPhotoEntity() = PhotoEntity(
    id = id,
    url = src.original ?: "",
    photographerName = photographer ?: "",
    alt = alt ?: ""
)

fun PhotoEntity.toPhoto() = Photo(
    id = id,
    url = url,
    photographerName = photographerName,
    alt = alt
)

fun Notification.toNotificationEntity() = NotificationEntity(
    id = id ?: 0,
    title = title,
    description = description,
    date = date,
    username = username,
    url = url
)

fun List<NotificationEntity>.toNotificationModel(): List<Notification> {

    return this.map {
        Notification(
            id = it.id,
            title = it.title,
            description = it.description,
            date = it.date,
            username = it.username,
            url = it.url
        )
    }
}