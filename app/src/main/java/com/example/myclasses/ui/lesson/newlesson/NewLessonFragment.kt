package com.example.myclasses.ui.lesson.newlesson

import android.app.AlertDialog
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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myclasses.R
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.DialogPictureListBinding
import com.example.myclasses.databinding.FragmentNewLessonBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

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

        getPictureList()

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


        binding.lessonIconPicture.setOnClickListener {
            val dialog = AlertDialog.Builder(context).create()

            val dialogBinding = DialogPictureListBinding.inflate(layoutInflater)
            dialogBinding.pictureList.layoutManager = GridLayoutManager(context, 4)

            val adapter = PictureListAdapter(PictureClickListener { pictureName ->
                viewModel.setImageName(pictureName, resources)
                dialog.dismiss()
            })

            adapter.submitList(viewModel.pictureList.value)
            dialogBinding.pictureList.adapter = adapter

            dialog.setView(dialogBinding.root)
            dialog.show()
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

    private fun getPictureList(){
        var i = 0
        val list = ArrayList<String>()
        val prefix = resources.getString(R.string.defaultIconNamePrefix)
        while (true){
            try {
                val iconName : String = prefix + i++
                val id = resources.getIdentifier(iconName, "drawable", context?.packageName)
                if (id != 0){
                    list.add(iconName)
                } else {
                    setPictureList(list)
                    return
                }
            } catch (e: Exception){
                setPictureList(list)
                return
            }
        }
    }

    private fun setPictureList(list: List<String>){
        viewModel.setPictureList(list)
    }
}