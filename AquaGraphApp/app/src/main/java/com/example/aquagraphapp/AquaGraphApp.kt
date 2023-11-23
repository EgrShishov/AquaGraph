package com.example.aquagraphapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.aquagraphapp.notifications.NotificationService


class AquaGraphApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationService.CHANNEL_ID,
            "ScheduledWorks",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for notify users about scheduled works"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}