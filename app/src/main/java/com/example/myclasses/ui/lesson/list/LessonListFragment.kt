package com.example.myclasses.ui.lesson.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.FragmentLessonListBinding

class LessonListFragment : Fragment() {

    private lateinit var binding: FragmentLessonListBinding

    private lateinit var viewModel: LessonListViewModel
    private lateinit var viewModelFactory: LessonListViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonListBinding.inflate(inflater, container, false)

        val dataSource =
            activity?.application?.let { LessonsDatabase.getInstance(it).lessonsDatabaseDao }!!

        viewModelFactory = LessonListViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LessonListViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = LessonListAdapter(LessonWithTeacherClickListener { lessonWithTeacher ->
            val action =
                LessonListFragmentDirections.actionLessonListFragmentToLessonDetailsFragment(
                    lessonWithTeacher.lesson.lessonId
                )
            findNavController().navigate(action)
        })
        binding.lessonList.adapter = adapter

        viewModel.lessonsWithTeacher.observe(viewLifecycleOwner, {
            it?.let { lessonsWithTeachers ->
                adapter.submitList(lessonsWithTeachers)
            }
        })

        binding.addLesson.setOnClickListener {
            val action = LessonListFragmentDirections.actionLessonListFragmentToNewLessonFragment()
            findNavController().navigate(action)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}