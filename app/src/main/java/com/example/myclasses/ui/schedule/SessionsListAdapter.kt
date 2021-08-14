package com.example.myclasses.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.database.entities.relations.SessionLessonTeacher
import com.example.myclasses.databinding.CardSessionBinding

class SessionsListAdapter(private val clickListener: SessionClickListener) :
    ListAdapter<SessionLessonTeacher, SessionsListAdapter.ViewHolder>(SessionDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(private val binding: CardSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SessionLessonTeacher, clickListener: SessionClickListener) {
            binding.sessionLessonTeacher = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardSessionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SessionDiffCallBack : DiffUtil.ItemCallback<SessionLessonTeacher>() {

    override fun areItemsTheSame(
        oldItem: SessionLessonTeacher,
        newItem: SessionLessonTeacher
    ): Boolean {
        return oldItem.session.sessionId == newItem.session.sessionId
    }

    override fun areContentsTheSame(
        oldItem: SessionLessonTeacher,
        newItem: SessionLessonTeacher
    ): Boolean {
        return oldItem == newItem
    }
}

class SessionClickListener(val clickListener: (sessionLessonTeacher: SessionLessonTeacher) -> Unit) {
    fun onClick(sessionLessonTeacher: SessionLessonTeacher) = clickListener(sessionLessonTeacher)
}