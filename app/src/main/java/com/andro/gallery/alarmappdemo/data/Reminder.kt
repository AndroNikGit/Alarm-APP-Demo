package com.andro.gallery.alarmappdemo.data


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val details: String,
    val timeMillis: Long // UTC epoch millis when the alarm should fire
)