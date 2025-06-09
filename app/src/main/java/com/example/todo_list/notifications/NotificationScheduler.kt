package com.example.todo_list.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.todo_list.data.model.Task
import com.example.todo_list.sharedPreferences.SharedPreferencesHelper
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class NotificationScheduler(private val context: Context, private val prefs: SharedPreferencesHelper) {

    fun scheduleNotification(task: Task) {
        val minutesBefore = prefs.getNotificationTime().toIntOrNull() ?: 30
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val canSchedule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

        if (!canSchedule) {
            Toast.makeText(context, "App can't schedule exact alarms. Please check system settings.", Toast.LENGTH_LONG).show()
            return
        }

        val notificationTimeMillis = task.destinationDate
            .minusMinutes(minutesBefore.toLong())
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        Log.d("Mydebug", "${LocalDateTime.ofInstant(
                Instant.ofEpochMilli(notificationTimeMillis),
            ZoneId.systemDefault()
        )}")

        if (notificationTimeMillis < System.currentTimeMillis()) {
            Log.d("Mydebug", "Nie ustawiono alarmu - czas już minął.")
            return
        }


        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("task_id", task.id)
            putExtra("task_title", task.title)
            putExtra("task_description", task.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notificationTimeMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            Toast.makeText(context, "Permission denied to set exact alarm.", Toast.LENGTH_LONG).show()
        }
    }

    fun cancelNotification(task: Task) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("task_id", task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

}
