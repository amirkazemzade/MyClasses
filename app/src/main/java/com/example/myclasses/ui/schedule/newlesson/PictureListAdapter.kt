package com.example.myclasses.ui.schedule.newlesson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.databinding.CardPictureBinding

class PictureListAdapter(private val clickListener: PictureClickListener) :
    ListAdapter<String, PictureListAdapter.ViewHolder>(PictureDIffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(private val binding: CardPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, clickListener: PictureClickListener) {
            binding.pictureName = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardPictureBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class PictureDIffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class PictureClickListener(val clickListener: (pictureName: String) -> Unit) {
    fun onClick(pictureName: String) = clickListener(pictureName)
}