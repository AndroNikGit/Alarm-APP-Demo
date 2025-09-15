package com.andro.gallery.alarmappdemo.data

class ReminderRepository(private val dao: ReminderDao) {
    fun getAll() = dao.getAll()
    suspend fun getById(id: Long) = dao.getById(id)
    suspend fun insert(r: Reminder) = dao.insert(r)
    suspend fun update(r: Reminder) = dao.update(r)
    suspend fun delete(r: Reminder) = dao.delete(r)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
}