package com.example.myclasses.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.R
import com.example.myclasses.databinding.FragmentSettingsBinding
import com.example.myclasses.getOddOrEvenId
import com.example.myclasses.select

class SettingsFragment : Fragment() {

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
            binding.firstDayOfWeekDropMenu.setAdapter(adapter)
        }
        binding.firstDayOfWeekDropMenu.select(
            (viewModel.settings.value?.firstDayOfWeek?.minus(1)!!)
        )
        binding.firstDayOfWeekDropMenu.setOnItemClickListener { _, _, position, _ ->
            viewModel.settings.value?.firstDayOfWeek = position + 1
            viewModel.settings.value
                ?.let { settings -> getOddOrEvenId(settings.isWeekEven) }
                ?.let { pos -> binding.weekStateDropMenu.select(pos) }
        }

        ArrayAdapter.createFromResource(
            view.context, R.array.odd_and_even, android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            binding.weekStateDropMenu.setAdapter(adapter)
        }
        binding.weekStateDropMenu.select(getOddOrEvenId(viewModel.settings.value?.isWeekEven!!))
        binding.weekStateDropMenu.setOnItemClickListener { _, _, position, _ ->
            viewModel.settings.value?.isWeekEven = position == 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPreferences(): SharedPreferences {
        return activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
    }
}
