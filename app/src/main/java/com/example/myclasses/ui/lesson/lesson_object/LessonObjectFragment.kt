package com.example.myclasses.ui.lesson.lesson_object

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myclasses.R
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.FragmentClassesOfDayBinding
import com.example.myclasses.ui.lesson.LessonsListAdapter
import com.example.myclasses.ui.lesson.lesson_collection.LessonCollectionFragment
import com.example.myclasses.ui.lesson.lesson_collection.LessonCollectionFragmentDirections

class LessonObjectFragment(private val position: Int) : Fragment() {
    private var _binding: FragmentClassesOfDayBinding? = null
    private val binding get() = _binding!!

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

        _binding = FragmentClassesOfDayBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        backToTodayState()

        val adapter = LessonsListAdapter()
        binding.lessonsList.adapter = adapter
        viewModel.todayLessons.observe(viewLifecycleOwner, { value ->
            value?.let {
                adapter.data = value
            }
        })

        viewModel.navigateToNewLesson.observe(viewLifecycleOwner, { day ->
            day?.let {
                val action = LessonCollectionFragmentDirections.actionNavLessonToNewLessonFragment()
                action.dayId = day
                this.findNavController().navigate(action)
                doneNavigatingToNewLesson()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun getPreferences(): SharedPreferences{
        return activity?.getPreferences(Context.MODE_PRIVATE)!!
    }

    private fun backToTodayState() {
        if (viewModel.dayDifference.value!! < 0) {
            binding.backToToday.visibility = View.VISIBLE
            binding.backToToday.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_triangle_left,
                    activity?.theme
                )
            )
        } else if (viewModel.dayDifference.value!! > 0) {
            binding.backToToday.visibility = View.VISIBLE
            binding.backToToday.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_triangle_right,
                    activity?.theme
                )
            )
        }
    }

    private fun doneNavigatingToNewLesson() {
        viewModel.doneNavigatingToNewLesson()
    }
}