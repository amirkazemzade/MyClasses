package com.example.myclasses.ui.lesson.editlesson

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
import com.example.myclasses.database.entities.Teacher
import com.example.myclasses.databinding.DialogPictureListBinding
import com.example.myclasses.databinding.FragmentEditLessonBinding
import com.example.myclasses.ui.lesson.newlesson.AddSessionListAdapter
import com.example.myclasses.ui.lesson.newlesson.PictureClickListener
import com.example.myclasses.ui.lesson.newlesson.PictureListAdapter
import com.example.myclasses.ui.lesson.newlesson.SessionDeleteClickListener

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

        binding.teacherNameInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.getTeacher(text.toString())
        }

        viewModel.currentLesson.observe(viewLifecycleOwner, { lesson ->
            lesson?.let {
                binding.lessonNameEditText.setText(lesson.lessonName)
                binding.description.editText?.setText(lesson.description)
                viewModel.getSessions()
                viewModel.getTeacher()
            }
        })

        viewModel.currentSessions.observe(viewLifecycleOwner, { list ->
            val adapter = AddSessionListAdapter(SessionDeleteClickListener {
                viewModel.removeSession(it)
            })
            adapter.submitList(list)
            binding.sessionsList.adapter = adapter
        })

        viewModel.currentTeacher.observe(viewLifecycleOwner, { teacher ->
            if (teacher != null) {
                if (binding.teacherNameInputLayout.editText?.text.toString() != teacher.name)
                    binding.teacherNameInputLayout.editText?.setText(teacher.name)
                binding.teacherEmailInputLayout.let {
                    it.editText?.setText(teacher.email)
                    if (teacher.email != "")
                        it.visibility = View.VISIBLE
                }

                binding.teacherPhoneInputLayout.let {
                    if (teacher.phoneNumber != -1) {
                        it.editText?.setText(teacher.phoneNumber.toString())
                        it.visibility = View.VISIBLE
                    } else
                        it.editText?.setText("")
                }
                binding.teacherAddressInputLayout.let {
                    it.editText?.setText(teacher.address)
                    if (teacher.address != "")
                        it.visibility = View.VISIBLE
                }
                binding.teacherWebsiteInputLayout.let {
                    it.editText?.setText(teacher.websiteAddress)
                    if (teacher.websiteAddress != "")
                        it.visibility = View.VISIBLE
                }
            } else {
                if (!binding.teacherNameInputLayout.editText?.isFocused!!)
                    binding.teacherNameInputLayout.editText?.setText("")
                binding.teacherEmailInputLayout.editText?.setText("")
                binding.teacherPhoneInputLayout.editText?.setText("")
                binding.teacherAddressInputLayout.editText?.setText("")
                binding.teacherWebsiteInputLayout.editText?.setText("")
                binding.teacherFieldsGroup.visibility = View.GONE
            }
            viewModel.refreshIsTeacherMenuExpanded()
        })

        viewModel.isTeacherMenuExpended.observe(viewLifecycleOwner) { value ->
            when (value) {
                false -> {
                    val noTeacher = binding.teacherNameInputLayout.editText?.text.toString() == ""
                    binding.teacherMenu.setImageResource(R.drawable.ic_arrow_down_24)
                    binding.teacherEmailInputLayout.let {
                        if (noTeacher) it.editText?.setText("")
                        if (it.editText?.text.toString() == "") it.visibility = View.GONE
                    }
                    binding.teacherPhoneInputLayout.let {
                        if (noTeacher) it.editText?.setText("")
                        if (it.editText?.text.toString() == "") it.visibility = View.GONE
                    }
                    binding.teacherAddressInputLayout.let {
                        if (noTeacher) it.editText?.setText("")
                        if (it.editText?.text.toString() == "") it.visibility = View.GONE
                    }
                    binding.teacherWebsiteInputLayout.let {
                        if (noTeacher) it.editText?.setText("")
                        if (it.editText?.text.toString() == "") it.visibility = View.GONE
                    }
                }
                true -> {
                    binding.teacherMenu.setImageResource(R.drawable.ic_arrow_up_24)
                    binding.teacherEmailInputLayout.let {
                        if (it.editText?.text.toString() == "") it.visibility = View.VISIBLE
                    }
                    binding.teacherPhoneInputLayout.let {
                        if (it.editText?.text.toString() == "") it.visibility = View.VISIBLE
                    }
                    binding.teacherAddressInputLayout.let {
                        if (it.editText?.text.toString() == "") it.visibility = View.VISIBLE
                    }
                    binding.teacherWebsiteInputLayout.let {
                        if (it.editText?.text.toString() == "") it.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.teachers.observe(viewLifecycleOwner, { teachers ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                teachers
            )
            binding.teacherNameMenu.setAdapter(adapter)
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
            setHomeAsUpIndicator(R.drawable.ic_baseline_clear_24)
            title = getString(R.string.edit_lesson)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    // action menu configurations
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

    private fun onSaveButton() {
        val lessonName = binding.lessonNameInputLayout.editText?.text.toString()
        val des = binding.description.editText?.text.toString()
        val teacherName = binding.teacherNameInputLayout.editText?.text.toString()
        val teacherEmail = binding.teacherEmailInputLayout.editText?.text.toString()
        val teacherPhoneText = binding.teacherPhoneInputLayout.editText?.text.toString()
        val teacherPhone =
            try {
                teacherPhoneText.toInt()
            } catch (e: Exception) {
                -1
            }
        val teacherAddress = binding.teacherAddressInputLayout.editText?.text.toString()
        val teacherWebsite = binding.teacherWebsiteInputLayout.editText?.text.toString()
        val teacher =
            Teacher(0, teacherName, teacherEmail, teacherPhone, teacherAddress, teacherWebsite)

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
                viewModel.onSaveButton(lessonName, des, teacher)
            }
        }
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
}