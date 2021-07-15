package com.example.myclasses.ui.lesson

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.example.myclasses.R
import com.example.myclasses.convertTimeInLongToFormatted
import com.example.myclasses.database.Lesson

@BindingAdapter("lessonIcon")
fun ImageView.setLessonIcon(item: Lesson?){
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
fun TextView.setTimeFormatted(item: Lesson?){
    item?.let {
        text = convertTimeInLongToFormatted(item.startTime, item.endTime)
    }
}

@BindingAdapter("pictureIcon")
fun ImageView.setPictureIcon(pictureName: String?){
    pictureName?.let {
        val id = resources.getIdentifier(pictureName, "drawable", context.packageName)
        setImageDrawable(AppCompatResources.getDrawable(context, id))
    }
}