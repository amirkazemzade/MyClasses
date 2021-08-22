package com.example.myclasses.ui.teacher.edit

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myclasses.R
import com.example.myclasses.database.LessonsDatabase
import com.example.myclasses.database.entities.Teacher
import com.example.myclasses.databinding.FragmentEditTeacherBinding

class EditTeacherFragment : Fragment() {
    private lateinit var binding: FragmentEditTeacherBinding
    private lateinit var viewModel: EditTeacherViewModel
    private lateinit var viewModelFactory: EditTeacherViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTeacherBinding.inflate(inflater, container, false)

        val dataSource =
            activity?.application?.let { LessonsDatabase.getInstance(it).lessonsDatabaseDao }!!
        val args = EditTeacherFragmentArgs.fromBundle(requireArguments())

        viewModelFactory = EditTeacherViewModelFactory(args.teacherId, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditTeacherViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.teacherNameInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.checkIfNameIsAvailable(text.toString())
        }

        viewModel.teacher.observe(viewLifecycleOwner) {
            it?.let { teacher ->
                binding.teacherNameInputLayout.editText?.setText(teacher.name)
                binding.teacherEmailInputLayout.editText?.setText(teacher.email)
                if (teacher.phoneNumber != -1)
                    binding.teacherPhoneInputLayout.editText?.setText(teacher.phoneNumber.toString())
                binding.teacherAddressInputLayout.editText?.setText(teacher.address)
                binding.teacherWebsiteInputLayout.editText?.setText(teacher.websiteAddress)
            }
        }

        viewModel.isNameAvailable.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    if (binding.teacherNameInputLayout.isErrorEnabled) {
                        binding.teacherNameInputLayout.isErrorEnabled = false
                        binding.teacherNameInputLayout.error = ""
                    }
                } else {
                    binding.teacherNameInputLayout.isErrorEnabled = true
                    binding.teacherNameInputLayout.error = "This Name Is Not Available!"
                }
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
        val phoneText = binding.teacherPhoneInputLayout.editText?.text.toString()
        val phone = try {
            phoneText.toInt()
        } catch (e: Exception) {
            -1
        }
        val address = binding.teacherAddressInputLayout.editText?.text.toString()
        val website = binding.teacherWebsiteInputLayout.editText?.text.toString()
        if (name == "") {
            binding.teacherNameInputLayout.isErrorEnabled = true
            binding.teacherNameInputLayout.error = "Please Enter A Name"
        } else if (viewModel.isNameAvailable.value == true) {
            val teacher = Teacher(0, name, email, phone, address, website)
            viewModel.onSaveButton(teacher)
        }
    }
}