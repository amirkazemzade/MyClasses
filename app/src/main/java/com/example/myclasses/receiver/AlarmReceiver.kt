package com.example.myclasses.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myclasses.MainActivity
import com.example.myclasses.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val sessionId = intent?.getLongExtra("session_id", 0)
        val lessonId = intent?.getLongExtra("lesson_id", 0)
        val lessonName = intent?.getStringExtra("lesson_name")

        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("move_to_lesson", true)
            putExtra("lesson_id", lessonId)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0)

        context?.let { myContext ->
            val notification = NotificationCompat.Builder(
                myContext,
                myContext.getString(R.string.session_reminders_id)
            )
                .setSmallIcon(R.drawable.ic_launcher_new_foreground_large)
                .setContentTitle("You Have Class!")
                .setContentText("Your $lessonName Class Starts Soon!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
            val notificationManager = NotificationManagerCompat.from(myContext)
            notificationManager.notify((sessionId?.toInt()!!), notification)
        }
    }
}