package com.example.myclasses

import android.widget.AutoCompleteTextView
import java.text.DateFormat
import java.util.*

fun convertTimeInLongToFormatted(startTime: Long, endTime: Long): String {
    val startCalendar = Calendar.getInstance()
    startCalendar.timeInMillis = startTime
    val startTimeString =
        DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(startCalendar.timeInMillis))

    val endCalendar = Calendar.getInstance()
    endCalendar.timeInMillis = endTime
    val endTimeString =
        DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(endCalendar.timeInMillis))


    return "$startTimeString - $endTimeString"
}

fun getOddOrEvenId(isEven: Boolean): Int {
    return if (isEven) 0 else 1
}

fun AutoCompleteTextView.select(positon: Int) {
    val itemText = adapter.getItem(positon).toString()
    setText(itemText, false)
}