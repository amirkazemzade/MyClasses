package com.example.myclasses.ui.teacher.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.database.entities.Teacher
import com.example.myclasses.databinding.CardTeacherBinding

class TeacherListAdapter(private val clickListener: TeacherClickListener) :
    ListAdapter<Teacher, TeacherListAdapter.ViewHolder>(TeacherDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(private val binding: CardTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Teacher, clickListener: TeacherClickListener) {
            binding.teacher = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardTeacherBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TeacherDiffCallBack : DiffUtil.ItemCallback<Teacher>() {
    override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
        return oldItem.teacherId == newItem.teacherId
    }

    override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
        return oldItem == newItem
    }
}

class TeacherClickListener(val clickListener: (teacher: Teacher) -> Unit) {
    fun onClick(teacher: Teacher) = clickListener(teacher)
}