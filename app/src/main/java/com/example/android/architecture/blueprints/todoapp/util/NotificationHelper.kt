package com.example.android.architecture.blueprints.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.TodoApplication
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import kotlin.random.Random

/**
 * Created by Abdulmajeed Alyafey on 5/8/20.
 */
class NotificationHelper {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel =  NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "This is for testing"
            enableLights(true)
            enableVibration(true)
            lightColor = Color.BLUE
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "channel_test_id"
        const val CHANNEL_NAME = "General Channel"

        private val notificationManager by lazy {
            TodoApplication.getInstance().
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        fun sendNotification(context: Context) {
            val builder = getNotificationBuilder(context)
            notificationManager.notify(Random.nextInt(), builder.build())
        }

        private fun getNotificationBuilder(context: Context) = run {
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, TasksActivity::class.java), // You can set flags for the intent to clear task on starting.
                    PendingIntent.FLAG_CANCEL_CURRENT
            )
            NotificationCompat.Builder(
                    context,
                    CHANNEL_ID
            ).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle("This is a test title")
                setContentText("This is a test content")
                setAutoCancel(true)
                setVibrate(longArrayOf(500, 500, 500, 500, 500))
                setLights(Color.BLUE, 1, 1)
                setStyle(NotificationCompat.BigTextStyle())
                setContentIntent(pendingIntent)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun deleteChannel(channelId: String)
            = notificationManager.deleteNotificationChannel(channelId)
}