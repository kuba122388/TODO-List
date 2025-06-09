package com.example.todo_list.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.todo_list.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val taskId = intent.getIntExtra("task_id", 0)
        val title = intent.getStringExtra("task_title") ?: "Zadanie"
        val description = intent.getStringExtra("task_description") ?: ""

        val tapIntent = Intent(context, com.example.MainActivity::class.java).apply {
            putExtra("task_id", taskId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val tapPendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "todo_channel")
            .setSmallIcon(R.drawable.ic_background)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(tapPendingIntent)
            .build()


        NotificationManagerCompat.from(context).notify(taskId, notification)
    }

    fun createNotificationChannel(context: Context) {
        val name = "ToDo Notifications"
        val descriptionText = "Notifications for scheduled tasks"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("todo_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
