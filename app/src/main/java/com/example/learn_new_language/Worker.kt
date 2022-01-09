package com.example.learn_new_language

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

open class Worker (appContext: Context, workerParams: WorkerParameters)
    :Worker(appContext,workerParams) {

    override fun doWork(): Result {
        val msg = inputData.getString("new interesting stories by Arabic language ")
        createNotification("don't miss a Story !!", msg.toString())


        return Result.success()
    }


    private fun createNotification(title: String, description: String) {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)


            val notificationBuilder =
                NotificationCompat.Builder(applicationContext, "101")
                    .setContentTitle(title)
                    .setContentText(description)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(R.drawable.ic_launcher_background)

            notificationManager.notify(1, notificationBuilder.build())

        }
    }
}