package com.andro.gallery.alarmappdemo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY timeMillis ASC")
    fun getAll(): Flow<List<Reminder>>


    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getById(id: Long): Reminder?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: Reminder): Long


    @Update
    suspend fun update(reminder: Reminder)


    @Delete
    suspend fun delete(reminder: Reminder)


    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteById(id: Long)



    @Query("SELECT * FROM reminders ORDER BY name ASC")
    fun getByName(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders ORDER BY details ASC")
    fun getByDetails(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders ORDER BY timeMillis ASC")
    fun getByTime(): Flow<List<Reminder>>
}