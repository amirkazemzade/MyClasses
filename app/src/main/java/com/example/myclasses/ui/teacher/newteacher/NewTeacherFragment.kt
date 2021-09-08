package com.example.myclasses.ui.teacher.newteacher

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myclasses.R
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.database.entities.Teacher
import com.example.myclasses.databinding.FragmentNewTeacherBinding

class NewTeacherFragment : Fragment() {
    private lateinit var binding: FragmentNewTeacherBinding

    private lateinit var viewModel: NewTeacherViewModel
    private lateinit var viewModelFactory: NewTeacherViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTeacherBinding.inflate(inflater, container, false)

        val dataSource =
            activity?.application?.let { LessonsDatabase.getInstance(it).lessonsDatabaseDao }!!

        viewModelFactory = NewTeacherViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewTeacherViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.teacherNameInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            if (binding.teacherNameInputLayout.isErrorEnabled) {
                binding.teacherNameInputLayout.isErrorEnabled = false
                binding.teacherNameInputLayout.error = ""
            }
            viewModel.getTeacher(text.toString())
        }

        viewModel.teachers.observe(viewLifecycleOwner) {
            it?.let { teachers ->
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    teachers
                )
                binding.teacherNameMenu.setAdapter(adapter)
            }
        }

        viewModel.currentTeacher.observe(viewLifecycleOwner) { teacher ->
            if (teacher == null) {
                binding.teacherEmailInputLayout.editText?.setText("")
                binding.teacherPhoneInputLayout.editText?.setText("")
                binding.teacherAddressInputLayout.editText?.setText("")
                binding.teacherWebsiteInputLayout.editText?.setText("")
            } else {
                binding.teacherEmailInputLayout.editText?.setText(teacher.email)
                binding.teacherPhoneInputLayout.editText?.setText(teacher.phoneNumber)
                binding.teacherAddressInputLayout.editText?.setText(teacher.address)
                binding.teacherWebsiteInputLayout.editText?.setText(teacher.websiteAddress)
            }
        }

        viewModel.navigateUp.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigateUp()
                viewModel.onNavigateUpDone()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_baseline_clear_24)
            title = getString(R.string.add_new_lesson)
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
        val name = binding.teacherNameInputLayout.editText?.text.toString()
        val email = binding.teacherEmailInputLayout.editText?.text.toString()
        val phone = binding.teacherPhoneInputLayout.editText?.text.toString()
        val address = binding.teacherAddressInputLayout.editText?.text.toString()
        val website = binding.teacherWebsiteInputLayout.editText?.text.toString()
        if (name == "") {
            binding.teacherNameInputLayout.isErrorEnabled = true
            binding.teacherNameInputLayout.error = "Please Enter A Name"
        } else {
            val teacher = Teacher(0, name, email, phone, address, website)
            viewModel.onSaveButton(teacher)
        }
    }
}