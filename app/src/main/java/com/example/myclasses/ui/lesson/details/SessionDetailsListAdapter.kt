package com.example.myclasses.ui.lesson.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.database.entities.Session
import com.example.myclasses.databinding.CardSessionDetailsBinding
import com.example.myclasses.setSessionName


class SessionDetailsListAdapter :
    ListAdapter<Session, SessionDetailsListAdapter.ViewHolder>(SessionDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }


    class ViewHolder private constructor(private val binding: CardSessionDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Session, position: Int) {
            binding.session = item
            binding.sessionName.setSessionName(position)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardSessionDetailsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class SessionDiffCallBack : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.sessionId == newItem.sessionId
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem == newItem
    }
}