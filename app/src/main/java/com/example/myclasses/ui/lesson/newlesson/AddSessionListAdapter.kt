package com.example.myclasses.ui.lesson.newlesson

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myclasses.R
import com.example.myclasses.database.entities.Session
import com.example.myclasses.databinding.ItemSessionBinding
import com.example.myclasses.select
import java.util.*
import java.util.concurrent.TimeUnit

class AddSessionListAdapter(private val clickListener: SessionDeleteClickListener) :
    ListAdapter<Session, AddSessionListAdapter.ViewHolder>(SessionDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, clickListener)
    }

    class ViewHolder private constructor(
        private val binding: ItemSessionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        private val startCalendar = MutableLiveData<Calendar>()
        private val endCalendar = MutableLiveData<Calendar>()


        fun bind(item: Session, position: Int, clickListener: SessionDeleteClickListener) {
            binding.session = item
            binding.executePendingBindings()

            //Session Name
            binding.sessionName.text =
                context.resources.getString(R.string.session_name, position + 1)

            binding.deleteSession.setOnClickListener {
                clickListener.onClick(item)
            }

            // Day Of Week
            ArrayAdapter.createFromResource(
                context,
                R.array.days_of_week_long,
                android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                binding.dayOfWeekDropMenu.setAdapter(adapter)
            }
            binding.dayOfWeekDropMenu.setOnItemClickListener { _, _, pos, _ ->
                item.dayOfWeek = pos + 1
            }

            // Week State
            ArrayAdapter.createFromResource(
                context, R.array.week_state, android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                binding.weekStateDropMenu.setAdapter(adapter)
            }
            binding.weekStateDropMenu.setOnItemClickListener { _, _, pos, _ ->
                item.weekState = pos
            }

            // Start Time
            binding.startTimeTextView.setOnClickListener {
                if (startCalendar.value == null) startCalendar.value = Calendar.getInstance()
                val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    setStartTime(hourOfDay, minute, item)
                    binding.session = item
                }
                val hourOfDay = startCalendar.value?.get(Calendar.HOUR_OF_DAY)!!
                val minute = startCalendar.value?.get(Calendar.MINUTE)!!
                TimePickerDialog(context, timeListener, hourOfDay, minute, true).show()
            }

            // End Time
            binding.endTimeTextView.setOnClickListener {
                if (endCalendar.value == null) endCalendar.value = Calendar.getInstance()
                val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    setEndTime(hourOfDay, minute, item)
                    binding.session = item
                }
                val hourOfDay = endCalendar.value?.get(Calendar.HOUR_OF_DAY)!!
                val minute = endCalendar.value?.get(Calendar.MINUTE)!!
                TimePickerDialog(context, timeListener, hourOfDay, minute, true).show()
            }


            // Setting Session Values
            if (item.startHour >= 0 && item.startMin >= 0) {
                binding.dayOfWeekDropMenu.select(item.dayOfWeek - 1)
                binding.weekStateDropMenu.select(item.weekState)
                startCalendar.value = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, item.startHour)
                    set(Calendar.MINUTE, item.startMin)
                }
                endCalendar.value = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, item.endHour)
                    set(Calendar.MINUTE, item.endMin)
                }
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSessionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }

        private fun setStartTime(hourOfDay: Int, minute: Int, item: Session) {
            if (startCalendar.value == null) {
                startCalendar.value = Calendar.getInstance()
                val calendar = Calendar.getInstance()
                val oneHour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
                calendar.timeInMillis = calendar.timeInMillis + oneHour
                endCalendar.value = calendar
            }
            startCalendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startCalendar.value?.set(Calendar.MINUTE, minute)
            item.startHour = hourOfDay
            item.startMin = minute
        }

        private fun setEndTime(hourOfDay: Int, minute: Int, item: Session) {
            if (endCalendar.value == null) {
                val calendar = Calendar.getInstance()
                val oneHour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
                calendar.timeInMillis = calendar.timeInMillis + oneHour
                endCalendar.value = calendar
            }
            endCalendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endCalendar.value?.set(Calendar.MINUTE, minute)
            item.endHour = hourOfDay
            item.endMin = minute
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

class SessionDeleteClickListener(val clickListener: (session: Session) -> Unit) {
    fun onClick(session: Session) = clickListener(session)
}