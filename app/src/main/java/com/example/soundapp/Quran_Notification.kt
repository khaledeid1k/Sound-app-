package com.example.soundapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build


class Quran_Notification : Application() {
    companion object{
        val CHANNEL_ID = "exampleServiceChannel"

    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Example Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}