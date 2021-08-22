package com.example.myclasses.ui.lesson.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.database.entities.relations.LessonWithTeacher
import com.example.myclasses.databinding.CardLessonBinding

class LessonListAdapter(private val clickListener: LessonWithTeacherClickListener) :
    ListAdapter<LessonWithTeacher, LessonListAdapter.ViewHolder>(LessonWithTeacherDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(private val binding: CardLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LessonWithTeacher, clickListener: LessonWithTeacherClickListener) {
            binding.lessonWithTeacher = item
            binding.clickListener = clickListener
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

class LessonWithTeacherDiffCallBack : DiffUtil.ItemCallback<LessonWithTeacher>() {
    override fun areItemsTheSame(oldItem: LessonWithTeacher, newItem: LessonWithTeacher): Boolean {
        return oldItem.lesson.lessonId == newItem.lesson.lessonId
    }

    override fun areContentsTheSame(
        oldItem: LessonWithTeacher,
        newItem: LessonWithTeacher
    ): Boolean {
        return oldItem == newItem
    }
}

class LessonWithTeacherClickListener(val clickListener: (lessonWithTeacher: LessonWithTeacher) -> Unit) {
    fun onClick(lessonWithTeacher: LessonWithTeacher) = clickListener(lessonWithTeacher)
}