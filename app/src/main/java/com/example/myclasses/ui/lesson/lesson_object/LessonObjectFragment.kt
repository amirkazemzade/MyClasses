package com.example.myclasses.ui.lesson.lesson_object

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.FragmentClassesOfDayBinding
import com.example.myclasses.ui.lesson.LessonsListAdapter

class LessonObjectFragment(private val position: Int) : Fragment() {
    private lateinit var binding: FragmentClassesOfDayBinding

    private lateinit var viewModel: LessonObjectViewModel
    private lateinit var viewModelFactory: LessonObjectViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataSource =
            activity?.let { LessonsDatabase.getInstance(it.application).lessonsDatabaseDao }!!

        viewModelFactory = LessonObjectViewModelFactory(position, dataSource, getPreferences())
        viewModel = ViewModelProvider(this, viewModelFactory).get(LessonObjectViewModel::class.java)

        binding = FragmentClassesOfDayBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = LessonsListAdapter()
        binding.lessonsList.adapter = adapter
        viewModel.todayLessons.observe(viewLifecycleOwner, { value ->
            value?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    // gets shared preferences and returns it
    private fun getPreferences(): SharedPreferences {
        return activity?.getPreferences(Context.MODE_PRIVATE)!!
    }
}