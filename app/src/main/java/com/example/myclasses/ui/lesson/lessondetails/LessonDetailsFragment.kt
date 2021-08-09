package com.example.myclasses.ui.lesson.lessondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
            }
        })

        viewModel.sessions.observe(viewLifecycleOwner, {
            it?.let {
                val adapter = SessionDetailsListAdapter()
                adapter.submitList(it)
                binding.sessionsList.adapter = adapter
                binding.sessionsList.layoutManager = GridLayoutManager(context, 2)
            }
        })

        viewModel.navigateToEdit.observe(viewLifecycleOwner, { lesson ->
            lesson?.let {
                val action =
                    LessonDetailsFragmentDirections.actionLessonDetailsFragmentToEditLessonFragment(
                        it.lessonId
                    )
                findNavController().navigate(action)
                viewModel.onNavigateToEditDone()
            }
        })

        viewModel.navigateUp.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigateUp()
                viewModel.onNavigateUpDone()
            }
        })

        return binding.root
    }
}