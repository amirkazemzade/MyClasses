package com.example.myclasses

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.Teacher
import java.text.DateFormat
import java.util.*

@BindingAdapter("lessonIcon")
fun ImageView.setLessonIcon(item: Lesson?) {
    item?.let {
        var imageId =
            resources.getIdentifier(item.imageName, "drawable", context.packageName)
        if (imageId == 0)
            imageId = resources.getIdentifier(
                R.string.defaultIconName.toString(), "drawable", context.packageName
            )
        setImageDrawable(AppCompatResources.getDrawable(context, imageId))
    }
}

@BindingAdapter("timeFormatted")
fun TextView.setTimeFormatted(item: Session?) {
    item?.let {
        text = convertTimeInLongToFormatted(item)
    }
}

@BindingAdapter("pictureIcon")
fun ImageView.setPictureIcon(pictureName: String?) {
    pictureName?.let {
        val id = resources.getIdentifier(pictureName, "drawable", context.packageName)
        setImageDrawable(AppCompatResources.getDrawable(context, id))
    }
}

@BindingAdapter("startTimeAsString")
fun TextView.setStartTime(item: Session?) {
    item?.let {
        if (item.startHour >= 0 && item.startMin >= 0) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, item.startHour)
                set(Calendar.MINUTE, item.startMin)
            }
            text = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.timeInMillis)
        }
    }
}

@BindingAdapter("endTimeAsString")
fun TextView.setEndTime(item: Session?) {
    item?.let {
        if (item.endHour >= 0 && item.endMin >= 0) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, item.endHour)
                set(Calendar.MINUTE, item.endMin)
            }
            text = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.timeInMillis)
        }
    }
}

@BindingAdapter("dayOfWeek")
fun TextView.setDayOfWeek(item: Session?) {
    val days = resources.getStringArray(R.array.days_of_week_long)
    item?.let {
        text = days[item.dayOfWeek - 1]
    }
}

@BindingAdapter("weekState")
fun TextView.setWeekState(item: Session?) {
    val states = resources.getStringArray(R.array.week_state)
    item?.let {
        text = states[item.weekState]
    }
}

@BindingAdapter("sessionName")
fun TextView.setSessionName(position: Int) {
    text = resources.getString(R.string.session_name, position + 1)
}

@BindingAdapter("teacherName")
fun TextView.setTeacherName(item: Teacher?) {
    if (item != null) {
        text = item.name
        visibility = View.VISIBLE
    } else {
        text = ""
        visibility = View.GONE
    }
}