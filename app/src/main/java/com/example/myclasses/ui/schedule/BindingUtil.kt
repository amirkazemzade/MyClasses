package com.example.myclasses.ui.schedule

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.example.myclasses.R
import com.example.myclasses.convertTimeInLongToFormatted
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
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
        text = convertTimeInLongToFormatted(item.startTime, item.endTime)
    }
}

@BindingAdapter("pictureIcon")
fun ImageView.setPictureIcon(pictureName: String?) {
    pictureName?.let {
        val id = resources.getIdentifier(pictureName, "drawable", context.packageName)
        setImageDrawable(AppCompatResources.getDrawable(context, id))
    }
}

@BindingAdapter("timeAsString")
fun TextView.setTime(calendar: LiveData<Calendar>?) {
    calendar?.let {
        val date = calendar.value?.timeInMillis?.let { cal -> Date(cal) }
        text = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }
}

@BindingAdapter("startTimeAsString")
fun TextView.setStartTime(item: Session?) {
    item?.let {
        if (item.startTime >= 0) {
            text = DateFormat.getTimeInstance(DateFormat.SHORT).format(item.startTime)
        }
    }
}

@BindingAdapter("endTimeAsString")
fun TextView.setEndTime(item: Session?) {
    item?.let {
        if (item.endTime >= 0) {
            text = DateFormat.getTimeInstance(DateFormat.SHORT).format(item.endTime)
        }
    }
}