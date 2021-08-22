package com.example.myclasses.ui.lesson.details

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myclasses.convertLessonToStringForShare
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.FragmentLessonDetailsBinding

class LessonDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLessonDetailsBinding
    private lateinit var viewModel: LessonDetailsViewModel
    private lateinit var viewModelFactory: LessonDetailsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonDetailsBinding.inflate(inflater, container, false)

        val arguments =
            LessonDetailsFragmentArgs.fromBundle(requireArguments())
        val dataSource =
            activity?.application?.let { LessonsDatabase.getInstance(it).lessonsDatabaseDao }!!
        viewModelFactory = LessonDetailsViewModelFactory(arguments.lessonId, dataSource)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(LessonDetailsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.lesson.observe(viewLifecycleOwner, {
            it.let {
                viewModel.updateSessions()
                viewModel.updateTeacher()
            }
        })

        val adapter = SessionDetailsListAdapter()
        binding.sessionsList.adapter = adapter
        binding.sessionsList.layoutManager = GridLayoutManager(context, 2)

        viewModel.sessions.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.remove.setOnClickListener {
            onRemove()
        }

        binding.share.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                val lesson = viewModel.lesson.value!!
                val sessions = viewModel.sessions.value!!
                val teacher = viewModel.teacher.value
                putExtra(
                    Intent.EXTRA_TEXT,
                    convertLessonToStringForShare(lesson, sessions, teacher, resources)
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        viewModel.navigateToEdit.observe(viewLifecycleOwner) {
            it?.let { lesson ->
                val action = LessonDetailsFragmentDirections
                    .actionLessonDetailsFragmentToEditLessonFragment(lesson.lessonId)
                findNavController().navigate(action)
                viewModel.onNavigateToEditDone()
            }
        }

        viewModel.navigateToTeacherDetails.observe(viewLifecycleOwner) {
            it?.let { teacher ->
                val action = LessonDetailsFragmentDirections
                    .actionLessonDetailsFragmentToTeacherDetailsFragment(teacher.teacherId)
                findNavController().navigate(action)
                viewModel.onNavigateToTeacherDetailsDone()
            }
        }

        viewModel.navigateUp.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigateUp()
                viewModel.onNavigateUpDone()
            }
        })

        return binding.root
    }

    private fun onRemove() {
        val alertDialogBuilder = AlertDialog.Builder(context).apply {
            setMessage("Are you sure you want to Delete?")
            setPositiveButton("Yes") { _, _ ->
                viewModel.onRemove()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }
        alertDialogBuilder.create().show()

    }
}