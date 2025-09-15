package com.andro.gallery.alarmappdemo.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.andro.gallery.alarmappdemo.ui.AddEditReminderActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Reminder"
        val details = intent.getStringExtra("details") ?: "Don't forget!"
        val reminderId = intent.getLongExtra("id", -1L)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH
            )
            nm.createNotificationChannel(channel)
        }

        // 👉 Intent to open Edit screen
        val clickIntent = Intent(context, AddEditReminderActivity::class.java).apply {
            putExtra(AddEditReminderActivity.EXTRA_ID, reminderId)  // pass id
        }

        // Build proper back stack
        val stackBuilder = TaskStackBuilder.create(context).apply {
            addParentStack(AddEditReminderActivity::class.java) // define parent in Manifest
            addNextIntent(clickIntent)
        }

        val clickPendingIntent = stackBuilder.getPendingIntent(
            reminderId.toInt(),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(details)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(clickPendingIntent)   // 👉 attach intent
            .build()

        nm.notify(reminderId.toInt(), notification)
    }

    companion object {
        const val ACTION_ALARM = "com.example.reminder.ACTION_ALARM"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DETAILS = "extra_details"
        const val CHANNEL_ID = "reminder_channel"
    }
}