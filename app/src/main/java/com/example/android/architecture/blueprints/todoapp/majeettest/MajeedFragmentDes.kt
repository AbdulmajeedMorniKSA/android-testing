package com.example.android.architecture.blueprints.todoapp.majeettest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentMajeedDesBinding
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import kotlinx.android.synthetic.main.fragment_majeed_des.*
import kotlin.random.Random

class MajeedFragmentDes : Fragment() {

    private val args: MajeedFragmentDesArgs by navArgs()

    private lateinit var viewDataBinding: FragmentMajeedDesBinding

    private lateinit var notificationManager: NotificationManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_majeed_des, container, false)
        viewDataBinding = FragmentMajeedDesBinding.bind(view)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTest.text = args.testInfoArgs

        notify.setOnClickListener {
            sendNotification()
        }
    }

    private fun sendNotification() {
        notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel()
        val builder = getNotificationBuilder()
        notificationManager.notify(Random.nextInt(), builder.build())
    }

    private fun getNotificationBuilder() = run {
        val pendingIntent = PendingIntent.getActivity(
                requireContext(),
                0,
                Intent(context, TasksActivity::class.java), // You can set flags for the intent to clear task on starting.
                PendingIntent.FLAG_CANCEL_CURRENT
        )
        NotificationCompat.Builder(
                requireContext(),
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun deleteChannel(channelId: String) {
        notificationManager.deleteNotificationChannel(channelId)
    }
    
}
