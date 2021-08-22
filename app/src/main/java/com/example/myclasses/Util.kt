package com.example.myclasses

import android.content.res.Resources
import android.widget.AutoCompleteTextView
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.Teacher
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


    return "$startTimeString\n$endTimeString"
}

fun getOddOrEvenId(isEven: Boolean): Int {
    return if (isEven) 0 else 1
}

fun convertLessonToStringForShare(
    lesson: Lesson,
    sessions: List<Session>,
    teacher: Teacher?,
    res: Resources
): String {
    var text = "Lesson Name: ${lesson.lessonName}\n"
    teacher?.let { text += "Teacher Name: ${it.name}\n" }
    text += "Description: ${lesson.description}"
    var sessionId = 1
    sessions.forEach { session ->
        val weekState = when (session.weekState) {
            0 -> "Every Week"
            1 -> "Even Weeks"
            2 -> "Odd Weeks"
            else -> "Every Week"
        }
        val day = res.getStringArray(R.array.days_of_week_long)[session.dayOfWeek - 1]
        val time = convertTimeInLongToFormatted(session.startTime, session.endTime)
        text += "\n\nSession${sessionId++}:\n" +
                weekState +
                "\nOn ${day}s\n" +
                time
    }
    return text
}

fun convertTeacherToStringForShare(teacher: Teacher): String {
    var text = "Teacher Name: ${teacher.name}\n"
    if (teacher.email != "") text += "Email: ${teacher.email}\n"
    if (teacher.phoneNumber != -1) text += "Phone: ${teacher.phoneNumber}\n"
    if (teacher.address != "") text += "Address: ${teacher.address}\n"
    if (teacher.websiteAddress != "") text += "Website: ${teacher.websiteAddress}\n"

    return text
}

fun AutoCompleteTextView.select(position: Int) {
    val itemText = adapter.getItem(position).toString()
    setText(itemText, false)
}