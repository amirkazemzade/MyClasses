package com.example.myclasses.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.database.entities.relations.SessionWithLesson
import com.example.myclasses.databinding.CardSessionBinding

class SessionsListAdapter :
    ListAdapter<SessionWithLesson, SessionsListAdapter.ViewHolder>(LessonDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: CardSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SessionWithLesson) {
            binding.sessionWithLesson = item
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

class LessonDiffCallBack : DiffUtil.ItemCallback<SessionWithLesson>() {

    override fun areItemsTheSame(oldItem: SessionWithLesson, newItem: SessionWithLesson): Boolean {
        return oldItem.session.sessionId == newItem.session.sessionId
    }

    override fun areContentsTheSame(
        oldItem: SessionWithLesson,
        newItem: SessionWithLesson
    ): Boolean {
        return oldItem == newItem
    }
}