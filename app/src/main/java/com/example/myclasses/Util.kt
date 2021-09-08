package com.example.myclasses

import android.content.res.Resources
import android.widget.AutoCompleteTextView
import com.example.myclasses.database.Settings
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.Teacher
import java.text.DateFormat
import java.util.*

fun convertTimeInLongToFormatted(session: Session): String {
    val startCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, session.startHour)
        set(Calendar.MINUTE, session.startMin)
    }
    val startTimeString =
        DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(startCalendar.timeInMillis))

    val endCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, session.endHour)
        set(Calendar.MINUTE, session.endMin)
    }
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
        val time = convertTimeInLongToFormatted(session)
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
    if (teacher.phoneNumber != "") text += "Phone: ${teacher.phoneNumber}\n"
    if (teacher.address != "") text += "Address: ${teacher.address}\n"
    if (teacher.websiteAddress != "") text += "Website: ${teacher.websiteAddress}\n"

    return text
}

fun AutoCompleteTextView.select(position: Int) {
    val itemText = adapter.getItem(position).toString()
    setText(itemText, false)
}

fun Session.getNextSessionInMilli(settings: Settings): Long {
    val today = Calendar.getInstance()
    val startCal = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, startHour)
        set(Calendar.MINUTE, startMin)
    }
    val calendar = Calendar.getInstance().apply {
        firstDayOfWeek = settings.firstDayOfWeek
        set(Calendar.DAY_OF_WEEK, dayOfWeek)
        set(Calendar.HOUR_OF_DAY, startCal.get(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, startCal.get(Calendar.MINUTE) - 5)
        set(Calendar.SECOND, 0)
    }

    var daysToAdd = 0
    if ((weekState == 1 && !settings.isWeekEven) || (weekState == 2 && settings.isWeekEven))
        daysToAdd += 7
    else if (today.timeInMillis > calendar.timeInMillis) {
        daysToAdd += if (weekState != 0) 14 else 7
    }

    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + daysToAdd)

    return calendar.timeInMillis
}