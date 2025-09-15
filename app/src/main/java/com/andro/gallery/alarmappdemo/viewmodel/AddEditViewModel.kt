package com.andro.gallery.alarmappdemo.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.andro.gallery.alarmappdemo.alarm.AlarmScheduler
import com.andro.gallery.alarmappdemo.data.AppDatabase
import com.andro.gallery.alarmappdemo.data.Reminder
import com.andro.gallery.alarmappdemo.data.ReminderRepository
import kotlinx.coroutines.launch


class AddEditViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ReminderRepository(AppDatabase.get(app).reminderDao())


    val name = MutableLiveData("")
    val details = MutableLiveData("")
    val timeMillis = MutableLiveData<Long>(System.currentTimeMillis())


    suspend fun load(id: Long): Reminder? = repo.getById(id)

    fun save(existingId: Long? = null, onDone: (Long) -> Unit) {
        viewModelScope.launch {
            val reminder = Reminder(
                id = existingId ?: 0,
                name = name.value?.trim().orEmpty(),
                details = details.value?.trim().orEmpty(),
                timeMillis = timeMillis.value ?: System.currentTimeMillis()
            )
            val id = if (existingId == null) repo.insert(reminder) else {
                repo.update(reminder); reminder.id
            }
            // Schedule alarm
            AlarmScheduler.schedule(getApplication(), reminder.copy(id = id))
            onDone(id)
        }
    }

}