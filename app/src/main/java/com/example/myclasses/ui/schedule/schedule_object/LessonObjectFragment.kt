package com.example.myclasses.ui.schedule.schedule_object

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.FragmentSessionsOfDayBinding
import com.example.myclasses.ui.schedule.SessionClickListener
import com.example.myclasses.ui.schedule.SessionsListAdapter
import com.example.myclasses.ui.schedule.schedule_collection.LessonCollectionFragmentDirections

class LessonObjectFragment(private val position: Int) : Fragment() {
    private lateinit var binding: FragmentSessionsOfDayBinding

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

        binding = FragmentSessionsOfDayBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = SessionsListAdapter(SessionClickListener { sessionWithLesson ->
            val action =
                LessonCollectionFragmentDirections.actionNavScheduleToLessonDetailsFragment(
                    sessionWithLesson.lesson.lessonName
                )
            findNavController().navigate(action)
        })
        binding.lessonsList.adapter = adapter
        viewModel.todaySessions.observe(viewLifecycleOwner, { value ->
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