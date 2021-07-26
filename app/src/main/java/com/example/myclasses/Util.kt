package com.example.myclasses

import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun convertTimeInLongToFormatted(startTime: Long, endTime: Long): String {
    val startCalendar = Calendar.getInstance()
    startCalendar.timeInMillis = startTime
    val startTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(startCalendar.timeInMillis))

    val endCalendar = Calendar.getInstance()
    endCalendar.timeInMillis = endTime
    val endTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(startCalendar.timeInMillis))


    return "$startTimeString - $endTimeString"
}

fun getCurrentWeek(): Long {
    val currentTime = System.currentTimeMillis()
    val sevenDays = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS)
    return TimeUnit.DAYS.convert(currentTime, TimeUnit.MILLISECONDS)
}

fun getWeekAsString(id: Int, days: Array<String>): String {
    if (id >= 7 || id <= 0) return days[0]
    return days[id]
}

fun getDayArrayId(id: Int): Int {
    if (id == 7) return 0
    return id
}

fun getOddOrEvenId(isEven: Boolean): Int{
    return if (isEven) 0 else 1
}