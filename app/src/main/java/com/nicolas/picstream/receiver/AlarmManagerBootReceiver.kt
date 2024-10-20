package com.nicolas.picstream.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nicolas.picstream.helper.DailyScheduleNotificationHelper

class AlarmManagerBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
             DailyScheduleNotificationHelper(context)
        }
    }
}