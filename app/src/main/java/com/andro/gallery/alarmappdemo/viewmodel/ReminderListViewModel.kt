package com.andro.gallery.alarmappdemo.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.andro.gallery.alarmappdemo.alarm.AlarmScheduler
import com.andro.gallery.alarmappdemo.data.AppDatabase
import com.andro.gallery.alarmappdemo.data.Reminder
import com.andro.gallery.alarmappdemo.data.ReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ReminderListViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ReminderRepository(AppDatabase.get(app).reminderDao())
    val reminders = repo.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun delete(reminder: Reminder) = viewModelScope.launch {
        repo.delete(reminder)
        AlarmScheduler.cancel(getApplication(), reminder.id)
    }
}