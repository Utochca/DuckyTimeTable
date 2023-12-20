package com.android.duckytimetable

import android.app.Service
import android.content.Intent
import android.os.IBinder


class MyNotificationService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val name = intent?.getStringExtra("name") ?: "Default Title"
        val description = intent?.getStringExtra("description") ?: "Default Description"

        val notificationService = NotificationService(applicationContext)
        notificationService.showNotification(name, description)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}