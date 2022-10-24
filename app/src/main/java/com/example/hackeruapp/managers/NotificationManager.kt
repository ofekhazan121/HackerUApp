package com.example.hackeruapp.managers

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hackeruapp.LoginActivity
import com.example.hackeruapp.R

object NotificationManager {

    val channelId = "CHANNEL_ID"

    fun createNotificationChannel(context: Context) {
        val name = "Channel"
        val description = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId,name,importance)
        channel.description = description
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun displayNotification(context: Context, Size: Int) {
        createNotificationChannel(context)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("New Item Added")
            .setContentText("$Size Items have been in your list for 24 Hours")
            .setSmallIcon(R.drawable.temp_image)
            .build()

        val notificationCompat = NotificationManagerCompat.from(context)
        notificationCompat.notify(1,builder)
    }

    fun serviceNotification(context: Context): Notification {
        val pendingIntent = PendingIntent.getActivity(context,0, Intent(context,LoginActivity::class.java),PendingIntent.FLAG_IMMUTABLE)
        createNotificationChannel(context)
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Service is Running")
            .setContentText("your app is running in the background")
            .setSmallIcon(R.drawable.temp_image)
            .setContentIntent(pendingIntent)
            .build()
    }
}