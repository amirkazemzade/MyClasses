package com.example.myclasses.ui.lesson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.R
import com.example.myclasses.convertTimeInLongToFormatted
import com.example.myclasses.database.Lesson
import com.example.myclasses.databinding.CardLessonBinding

class LessonsListAdapter :
    ListAdapter<Lesson, LessonsListAdapter.ViewHolder>(LessonDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: CardLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Lesson) {
            binding.lesson = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardLessonBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class LessonDiffCallBack() : DiffUtil.ItemCallback<Lesson>() {

    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem.lessonId == newItem.lessonId
    }

    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem == newItem
    }
}