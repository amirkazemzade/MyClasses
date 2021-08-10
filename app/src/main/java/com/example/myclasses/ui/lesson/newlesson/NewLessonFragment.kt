package com.example.myclasses.ui.lesson.newlesson

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myclasses.R
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.database.entities.Session
import com.example.myclasses.databinding.DialogPictureListBinding
import com.example.myclasses.databinding.FragmentNewLessonBinding

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

        val dataSource =
            activity?.let { LessonsDatabase.getInstance(it.application).lessonsDatabaseDao }!!
        val arguments = NewLessonFragmentArgs.fromBundle(requireArguments())

        viewModelFactory = NewLessonViewModelFactory(arguments.tabId, arguments.dayId, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewLessonViewModel::class.java)

        getPictureList()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.lessonNameMenu.doOnTextChanged { text, _, _, _ ->
            if (binding.lessonNameInputLayout.isErrorEnabled) {
                binding.lessonNameInputLayout.isErrorEnabled = false
                binding.lessonNameInputLayout.error = ""
            }
            viewModel.getLessonWithSessions(text.toString())
        }

        viewModel.currentLesson.observe(viewLifecycleOwner, { lesson ->
            if (lesson != null) {
                binding.description.editText?.setText(lesson.description)
            } else {
                binding.description.editText?.setText("")
            }
        })

        viewModel.currentSessions.observe(viewLifecycleOwner, { list ->
            val adapter = AddSessionListAdapter(SessionDeleteClickListener {
                viewModel.removeSession(it)
            })
            adapter.submitList(list)
            binding.sessionsList.adapter = adapter
        })

        binding.lessonIconPicture.setOnClickListener {
            val dialog = AlertDialog.Builder(context).create()

            val dialogBinding = DialogPictureListBinding.inflate(layoutInflater)
            dialogBinding.pictureList.layoutManager = GridLayoutManager(context, 4)

            val adapter = PictureListAdapter(PictureClickListener { pictureName ->
                viewModel.setImageName(pictureName)
                dialog.dismiss()
            })

            adapter.submitList(viewModel.pictureList.value)
            dialogBinding.pictureList.adapter = adapter

            dialog.setView(dialogBinding.root)
            dialog.show()
        }

        binding.addNewSession.setOnClickListener {
            viewModel.addNewSession()
        }

        viewModel.navigateToLessonFragment.observe(viewLifecycleOwner, { value ->
            value?.let {
                val action = NewLessonFragmentDirections.actionNewLessonFragmentToNavLesson()
                action.currentTabId = value
                findNavController().navigate(action)
                viewModel.doneNavigatingToLessonFragment()
            }
        })

        viewModel.lessons.observe(viewLifecycleOwner, { lessons ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                lessons
            )
            binding.lessonNameMenu.setAdapter(adapter)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_baseline_clear_24)
            title = "Add New Lesson"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_save -> {
                onSaveButton()
            }
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // gets list of all lesson icon images available in resources
    private fun getPictureList() {
        var i = 0
        val list = ArrayList<String>()
        val prefix = resources.getString(R.string.defaultIconNamePrefix)
        while (true) {
            try {
                val iconName: String = prefix + i++
                val id = resources.getIdentifier(iconName, "drawable", context?.packageName)
                if (id != 0) {
                    list.add(iconName)
                } else {
                    setPictureList(list)
                    return
                }
            } catch (e: Exception) {
                setPictureList(list)
                return
            }
        }
    }

    private fun setPictureList(list: List<String>) {
        viewModel.setPictureList(list)
    }

    private fun onSaveButton() {
        val lessonName = binding.lessonNameInputLayout.editText?.text.toString()
        val des = binding.description.editText?.text.toString()
        if (lessonName == "") {
            binding.lessonNameInputLayout.isErrorEnabled = true
            binding.lessonNameInputLayout.error = "Please Enter A Name"
        } else {
            viewModel.onSaveButton(lessonName, des)
        }
    }
}