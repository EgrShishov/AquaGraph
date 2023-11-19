package com.example.aquagraphapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.appsearch.SearchResult.Builder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.aquagraphapp.MainActivity


class NotificationService(applicationContext: Context) {
    private val applicationContext = applicationContext
    private val notificationManager = getSystemService(
        applicationContext,
        NotificationManager::class.java
    ) as NotificationManager

    companion object {
        const val CHANNEL_ID = "ScheduledWorksChannel"
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "ScheduledWorks",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for notify users about scheduled works"

//        val notificationManager = getSystemService(
//            applicationContext,
//            NotificationManager::class.java
//        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(notifyText: String) {
        val activityIntent = Intent(applicationContext, MainActivity::class.java)
        val activityPandingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        ).setContentTitle("Плановые работы!")
            .setContentText(notifyText)
            .setContentIntent(activityPandingIntent)
            .build()

        notificationManager.notify(
            1, notification
        )
    }
}