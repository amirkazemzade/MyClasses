package com.example.myclasses.ui.lesson.newlesson

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myclasses.R
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.databinding.FragmentNewLessonBinding
import java.text.FieldPosition
import java.util.*

class NewLessonFragment : Fragment() {

    private lateinit var binding: FragmentNewLessonBinding
    private lateinit var viewModel: NewLessonViewModel
    private lateinit var viewModelFactory: NewLessonViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewLessonBinding.inflate(inflater, container, false)

        val dataSource = activity?.let { LessonsDatabase.getInstance(it.application).lessonsDatabaseDao }!!
        val arguments = NewLessonFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = NewLessonViewModelFactory(arguments.dayId, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewLessonViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ArrayAdapter.createFromResource(
            view.context, R.array.week_state, android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            binding.weekStateSpinner.adapter = adapter
        }

        binding.startTimePreview.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                setStartTime(hourOfDay, minute)
            }
            val hourOfDay = viewModel.startCalendar.value?.get(Calendar.HOUR_OF_DAY)!!
            val minute = viewModel.startCalendar.value?.get(Calendar.MINUTE)!!
            TimePickerDialog(this.context, timeListener, hourOfDay, minute, true).show()
        }

        binding.endTimePreview.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                setEndTime(hourOfDay, minute)
            }
            val hourOfDay = viewModel.endCalendar.value?.get(Calendar.HOUR_OF_DAY)!!
            val minute = viewModel.endCalendar.value?.get(Calendar.MINUTE)!!
            TimePickerDialog(this.context, timeListener, hourOfDay, minute, true).show()
        }

        binding.saveButton.setOnClickListener {
            if (binding.lessonNameEditText.text.toString() == ""){
                Toast.makeText(context, "Please Enter The Name Of Lesson!", Toast.LENGTH_LONG).show()
            } else{
                onSaveButton()
            }
        }

        viewModel.navigateToLessonFragment.observe(viewLifecycleOwner, { value ->
            value?.let {
                this.findNavController().navigate(NewLessonFragmentDirections.actionNewLessonFragmentToNavLesson())
                viewModel.doneNavigatingToLessonFragment()
            }
        })
    }

    private fun setStartTime(hourOfDay: Int, minute: Int) {
        viewModel.setStartTime(hourOfDay, minute)
    }

    private fun setEndTime(hourOfDay: Int, minute: Int) {
        viewModel.setEndTime(hourOfDay, minute)
    }

    private fun onSaveButton(){
        val lessonName = binding.lessonNameEditText.text.toString()
        val weekState= binding.weekStateSpinner.selectedItemPosition
        val des = binding.description.text.toString()
        viewModel.onSaveButton(lessonName, weekState, des)
    }
}