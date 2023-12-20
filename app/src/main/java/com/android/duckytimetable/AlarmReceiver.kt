package com.android.duckytimetable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("AlarmReceiver","reciving")
        val name = intent?.getStringExtra("EXTRA_MESSAGE_NAME") ?: return
        val description = intent.getStringExtra("EXTRA_MESSAGE_DESCRIPTION") ?: return
        val service = NotificationService(context)
//        service.showNotification(name, description)
        val serviceIntent = Intent(context, MyNotificationService::class.java).apply {
            putExtra("name", name)
            putExtra("description", description)
        }
        context.startService(serviceIntent)
        Log.d("AlarmReceiver","recived")
    }
}