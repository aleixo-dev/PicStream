package com.nicolas.picstream.data.mapper

import com.nicolas.picstream.data.local.entity.NotificationEntity
import com.nicolas.picstream.data.local.entity.PhotoEntity
import com.nicolas.picstream.data.model.Notification
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.model.PhotoUrl
import com.nicolas.picstream.data.model.Topic
import com.nicolas.picstream.data.response.PhotoResponse
import com.nicolas.picstream.data.response.TopicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun PhotoResponse.toDomain() = withContext(Dispatchers.IO) {
    Photo(
        slug = slug,
        id = id,
        url = PhotoUrl(
            raw = url.raw,
            full = url.full,
            regular = url.regular,
            small = url.small,
            thumb = url.thumb,
            smallS3 = url.smallS3
        )
    )
}

fun TopicResponse.toTopic() = Topic(
    id = id,
    slug = slug,
    title = title,
    status = status
)

suspend fun List<PhotoResponse>.toDomain() = map { it.toDomain() }


fun PhotoResponse.toPhotoEntity() = PhotoEntity(
    id = id,
    slug = slug,
    imageUrl = url.regular
)

fun PhotoEntity.toPhoto() = Photo(
    id = id,
    slug = slug,
    url = PhotoUrl(
        raw = "",
        full = "",
        small = "",
        thumb = "",
        smallS3 = "",
        regular = imageUrl,
    )
)

fun Notification.toNotificationEntity() = NotificationEntity(
    id = id ?: 0,
    title = title,
    description = description,
    date = date
)

fun List<NotificationEntity>.toNotificationModel(): List<Notification> {

    return this.map {
        Notification(
            id = it.id,
            title = it.title,
            description = it.description,
            date = it.date
        )
    }
}