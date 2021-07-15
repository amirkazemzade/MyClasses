package com.example.myclasses.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.*
import com.example.myclasses.database.Settings
import com.example.myclasses.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var viewModelFactory: SettingsViewModelFactory
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelFactory = SettingsViewModelFactory(getPreferences())
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ArrayAdapter.createFromResource(
            view.context,
            R.array.days_of_week_long,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            binding.daysOfWeek.adapter = adapter
        }
        binding.daysOfWeek.setSelection((viewModel.settings.value?.firstDayOfWeek?.minus(1)!!))
        binding.daysOfWeek.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            view.context, R.array.odd_and_even, android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            binding.oddOrEven.adapter = adapter
        }
        binding.oddOrEven.setSelection(getOddOrEvenId(viewModel.settings.value!!.isWeekEven))
        binding.oddOrEven.onItemSelectedListener = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (binding.daysOfWeek.selectedView == view) {
            viewModel.settings.value?.firstDayOfWeek = binding.daysOfWeek.selectedItemPosition + 1
            viewModel.settings.value?.let { getOddOrEvenId(it.isWeekEven) }?.let {
                binding.oddOrEven.setSelection(it)
            }
        } else {
            viewModel.settings.value?.isWeekEven = (binding.oddOrEven.selectedItemPosition == 0)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun getPreferences(): SharedPreferences {
        return activity?.getPreferences(Context.MODE_PRIVATE)!!
    }
}
