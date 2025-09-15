package com.andro.gallery.alarmappdemo.util


import java.text.SimpleDateFormat
import java.util.*


object DateTimeUtils {
    fun format(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}