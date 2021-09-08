package com.example.myclasses.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.database.Settings
import com.example.myclasses.getNextSessionInMilli
import kotlinx.coroutines.runBlocking

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            context?.let {
                val settings =
                    Settings(context.getSharedPreferences("settings", Context.MODE_PRIVATE))
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                runBlocking {
                    val dataSource = LessonsDatabase.getInstance(context).lessonsDatabaseDao
                    val sessionsWithLesson = dataSource.getSessions()
                    sessionsWithLesson.forEach { SWL ->
                        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                            putExtra("session_id", SWL.session.sessionId)
                            putExtra("lesson_id", SWL.lesson.lessonId)
                            putExtra("lesson_name", SWL.lesson.lessonName)
                        }
                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            SWL.session.sessionId,
                            alarmIntent,
                            0
                        )

                        val weekGap = if (SWL.session.weekState == 0) 1 else 2
                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            SWL.session.getNextSessionInMilli(settings),
                            AlarmManager.INTERVAL_DAY * 7 * weekGap,
                            pendingIntent
                        )
                    }
                }
            }
        }
    }
}