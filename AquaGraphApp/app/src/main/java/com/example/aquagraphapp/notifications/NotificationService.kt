package com.example.aquagraphapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.appsearch.SearchResult.Builder
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.IconCompat
import com.example.aquagraphapp.MainActivity
import com.example.aquagraphapp.R


class NotificationService(applicationContext: Context) {
    private val applicationContext = applicationContext
    private val notificationManager = getSystemService(
        applicationContext,
        NotificationManager::class.java
    ) as NotificationManager

    companion object {
        const val CHANNEL_ID = "ScheduledWorksChannel"
    }
    fun showNotification(notifyText: String) {
        val activityIntent = Intent(applicationContext, MainActivity::class.java)
        val activityPandingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        ).setContentTitle("Плановые работы!")
            .setContentText(notifyText)
            .setContentIntent(activityPandingIntent)
            .setSmallIcon(R.drawable.baseline_info_24)
            .build()

        notificationManager.notify(
            1, notification
        )
    }
}