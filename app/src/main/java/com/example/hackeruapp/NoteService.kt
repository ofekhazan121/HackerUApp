package com.example.hackeruapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.hackeruapp.managers.NotificationManager
import com.example.hackeruapp.repository.NoteRepository
import kotlin.concurrent.thread


class NoteService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1,NotificationManager.serviceNotification(this))
        myServiceFunction()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    fun myServiceFunction() {
        thread (start = true){
            while (true) {
                Thread.sleep(10000)
                val list = NoteRepository.getInstance(this).getAllForService()
                var size = 0
                for (l in list) {
                    if (System.currentTimeMillis() - l.creationTime > 86400000) {
                        size ++
                    }
                }
                    if (size > 0) {
                        NotificationManager.displayNotification(this, size)
                    }
            }
        }
    }


}