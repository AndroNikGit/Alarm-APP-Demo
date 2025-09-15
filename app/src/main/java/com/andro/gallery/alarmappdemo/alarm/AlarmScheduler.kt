package com.andro.gallery.alarmappdemo.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.andro.gallery.alarmappdemo.data.Reminder
import com.andro.gallery.alarmappdemo.receiver.AlarmReceiver

object AlarmScheduler {
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun schedule(context: Context, reminder: Reminder) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Intent for AlarmReceiver
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", reminder.name)
            putExtra("details", reminder.details)
            putExtra("id", reminder.id)
        }

        val pi = PendingIntent.getBroadcast(
            context,
            reminder.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Cancel previous alarms with same id
        am.cancel(pi)

        // Use AlarmClockInfo for exact alarm (API 21+)
        val info = AlarmManager.AlarmClockInfo(reminder.timeMillis, pi)

        am.setAlarmClock(info, pi)
    }


    fun cancel(context: Context, reminderId: Long) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmReceiver.ACTION_ALARM
                putExtra(AlarmReceiver.EXTRA_ID, reminderId)
            },
            pendingFlags()
        )
        am.cancel(pi)
        pi.cancel()
    }


    private fun pendingIntent(
        context: Context,
        requestCode: Long,
        reminder: Reminder
    ): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_ALARM
            putExtra(AlarmReceiver.EXTRA_ID, reminder.id)
            putExtra(AlarmReceiver.EXTRA_NAME, reminder.name)
            putExtra(AlarmReceiver.EXTRA_DETAILS, reminder.details)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode.toInt(),
            intent,
            pendingFlags()
        )
    }


    private fun pendingFlags(): Int {
        var flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flags =
            flags or PendingIntent.FLAG_IMMUTABLE
        return flags
    }
}