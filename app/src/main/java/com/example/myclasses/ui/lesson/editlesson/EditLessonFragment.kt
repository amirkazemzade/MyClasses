package com.example.myclasses.ui.lesson.editlesson

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.databinding.DialogPictureListBinding
import com.example.myclasses.databinding.FragmentEditLessonBinding
import com.example.myclasses.ui.lesson.newlesson.*

class EditLessonFragment : Fragment() {
    private lateinit var binding: FragmentEditLessonBinding
    private lateinit var viewModel: EditLessonViewModel
    private lateinit var viewModelFactory: EditLessonViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditLessonBinding.inflate(inflater, container, false)

        val dataSource =
            activity?.let { LessonsDatabase.getInstance(it.application).lessonsDatabaseDao }!!
        val arguments =
            EditLessonFragmentArgs.fromBundle(requireArguments())

        viewModelFactory = EditLessonViewModelFactory(arguments.lessonId, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditLessonViewModel::class.java)

        getPictureList()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.lessonNameEditText.doOnTextChanged { text, _, _, _ ->
            if (binding.lessonNameInputLayout.isErrorEnabled) {
                binding.lessonNameInputLayout.isErrorEnabled = false
                binding.lessonNameInputLayout.error = ""
            }
            viewModel.setLessonName(text.toString())
        }

        viewModel.currentLesson.observe(viewLifecycleOwner, { lesson ->
            lesson?.let {
                binding.lessonNameEditText.setText(lesson.lessonName)
                binding.description.editText?.setText(lesson.description)
                viewModel.getSessions()
            }
        })

        viewModel.currentSessions.observe(viewLifecycleOwner, { list ->
            val adapter = AddSessionListAdapter()
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

        viewModel.navigateUp.observe(viewLifecycleOwner, { value ->
            value?.let {
                findNavController().navigateUp()
                viewModel.doneNavigatingUp()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setHomeAsUpIndicator(com.example.myclasses.R.drawable.ic_baseline_clear_24)
            title = "Add New Lesson"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(com.example.myclasses.R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.example.myclasses.R.id.action_menu_save -> {
                onSaveButton()
            }
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onSaveButton() {
        val lessonName = binding.lessonNameInputLayout.editText?.text.toString()
        val des = binding.description.editText?.text.toString()
        when {
            lessonName == "" -> {
                binding.lessonNameInputLayout.isErrorEnabled = true
                binding.lessonNameInputLayout.error = "Please Enter A Name!"
            }
            viewModel.nameIsAlreadyTaken(lessonName) -> {
                binding.lessonNameInputLayout.isErrorEnabled = true
                binding.lessonNameInputLayout.error = "This Lesson Is Already Exist!"
            }
            else -> {
                viewModel.onSaveButton(lessonName, des)
            }
        }
    }

    // gets list of all lesson icon images available in resources
    private fun getPictureList() {
        var i = 0
        val list = ArrayList<String>()
        val prefix = resources.getString(com.example.myclasses.R.string.defaultIconNamePrefix)
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
}