package com.andro.gallery.alarmappdemo.receiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.andro.gallery.alarmappdemo.alarm.AlarmScheduler
import com.andro.gallery.alarmappdemo.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED
        ) {
            val dao = AppDatabase.get(context).reminderDao()
            CoroutineScope(Dispatchers.IO).launch {
                dao.getAll().collect { list ->
                    val now = System.currentTimeMillis()
                    list.filter { it.timeMillis > now }
                        .forEach { AlarmScheduler.schedule(context, it) }
                }
            }
        }
    }
}