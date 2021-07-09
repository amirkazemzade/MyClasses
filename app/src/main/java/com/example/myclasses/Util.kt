package com.example.myclasses

import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun convertTimeInLongToFormatted(startTime: Long, endTime: Long): String {
    var startMinutes = TimeUnit.MINUTES.convert(startTime, TimeUnit.MILLISECONDS)
    var startHours = TimeUnit.HOURS.convert(startTime, TimeUnit.MILLISECONDS)
    startHours %= 24
    startMinutes %= 60

    val endMinutes = TimeUnit.MINUTES.convert(startTime, TimeUnit.MILLISECONDS)
    val endHours = TimeUnit.HOURS.convert(startTime, TimeUnit.MILLISECONDS)

    return "$startHours:$startMinutes - $endHours:$endMinutes"
}

fun main() {
    val oneDay = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
    var calendar = Calendar.getInstance()
    var week = calendar.get(Calendar.WEEK_OF_YEAR)
    val da = Date()
    val d = DateFormat.getDateInstance(DateFormat.MEDIUM).format(da)
    val date = "${da}"
    print((-1%7))
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