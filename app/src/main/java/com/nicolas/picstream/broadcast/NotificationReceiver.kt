package com.nicolas.picstream.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.nicolas.picstream.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        context?.let { sendNotification(it) }
    }

    private fun sendNotification(context: Context) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.title_notification))
            .setContentText(context.getString(R.string.description_notification))
            .setSmallIcon(R.drawable.splash_icon)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(1, notification.build())
    }

    companion object {
        private const val CHANNEL_ID = "daily_notification_channel"
        private const val CHANNEL_NAME = "Daily Notification"
    }
}