package com.android.duckytimetable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
        val name = intent?.getStringExtra("EXTRA_MESSAGE_NAME") ?: return
        val description = intent.getStringExtra("EXTRA_MESSAGE_DESCRIPTION") ?: return
        val service = NotificationService(context)
        service.showNotification(name, description)
    }
}