package com.example.myclasses.ui.teacher.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.FragmentTeacherListBinding

class TeacherListFragment : Fragment() {
    private lateinit var binding: FragmentTeacherListBinding

    private lateinit var viewModel: TeacherListViewModel
    private lateinit var viewModelFactory: TeacherListViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherListBinding.inflate(inflater, container, false)

        val dataSource =
            activity?.application?.let { LessonsDatabase.getInstance(it).lessonsDatabaseDao }!!
        viewModelFactory = TeacherListViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TeacherListViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = TeacherListAdapter(TeacherClickListener { teacher ->
            val action =
                TeacherListFragmentDirections.actionTeacherListFragmentToTeacherDetailsFragment(
                    teacher.teacherId
                )
            findNavController().navigate(action)
        })
        binding.teacherList.adapter = adapter

        binding.addTeacher.setOnClickListener {
            viewModel.onAddTeacher()
        }

        viewModel.teachers.observe(viewLifecycleOwner, {
            it?.let { teachers ->
                adapter.submitList(teachers)
            }
        })

        viewModel.navigateToNewTeacher.observe(viewLifecycleOwner) {
            it?.let {
                val action =
                    TeacherListFragmentDirections.actionTeacherListFragmentToNewTeacherFragment()
                findNavController().navigate(action)
                viewModel.onNavigateToNewTeacherDone()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}