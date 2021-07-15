package com.example.myclasses.ui.lesson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.R
import com.example.myclasses.convertTimeInLongToFormatted
import com.example.myclasses.database.Lesson

class LessonsListAdapter : RecyclerView.Adapter<LessonsListAdapter.ViewHolder>() {
    var data = listOf<Lesson>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lessonIcon: ImageView = itemView.findViewById(R.id.lessonIcon)
        private val lessonName: TextView = itemView.findViewById(R.id.lessonName)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val description: TextView = itemView.findViewById(R.id.description)

        fun bind(item: Lesson) {
            val res = itemView.context.resources
            var imageId =
                res.getIdentifier(item.imageName, "drawable", itemView.context.packageName)
            if (imageId == 0)
                imageId = res.getIdentifier(
                R.string.defaultIconName.toString(), "drawable", itemView.context.packageName
            )
            lessonIcon.setImageDrawable(getDrawable(itemView.context, imageId))
            lessonName.text = item.lessonName
            time.text = convertTimeInLongToFormatted(item.startTime, item.endTime)
            description.text = item.description
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.card_lesson, parent, false)
                return ViewHolder(view)
            }
        }
    }
}