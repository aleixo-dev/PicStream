package com.nicolas.picstream.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nicolas.picstream.utils.Notification

class DailyNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("DailyNotificationReceiver", "Alarme disparado")
        Notification.send(context)
    }
}