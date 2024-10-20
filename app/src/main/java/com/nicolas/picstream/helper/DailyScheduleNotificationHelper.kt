package com.nicolas.picstream.helper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.nicolas.picstream.manager.NotificationFlag
import com.nicolas.picstream.receiver.DailyNotificationReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class DailyScheduleNotificationHelper(private val context: Context) : KoinComponent {

    private val notificationFlag by inject<NotificationFlag>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            scheduleNotification()
        }
    }

    @SuppressLint("ObsoleteSdkInt", "MissingPermission")
    private suspend fun scheduleNotification() {

        val shouldScheduleNotification = withContext(Dispatchers.IO) {
            notificationFlag.isNotificationEnable.first()
        }

        if (!shouldScheduleNotification) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(context, DailyNotificationReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                10000,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}