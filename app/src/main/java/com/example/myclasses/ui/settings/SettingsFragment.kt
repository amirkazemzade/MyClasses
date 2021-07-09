package com.example.myclasses.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.*
import com.example.myclasses.database.Settings
import com.example.myclasses.databinding.FragmentSettingsBinding
import kotlin.math.log

class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var settings: Settings

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        settings = activity?.let { Settings(it.getPreferences(Context.MODE_PRIVATE)) }!!

        ArrayAdapter.createFromResource(
            view.context,
            R.array.days_of_week_long,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            binding.daysOfWeek.adapter = adapter
        }
        binding.daysOfWeek.setSelection(settings.firstDayOfWeek - 1)
        binding.daysOfWeek.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            view.context,
            R.array.odd_and_even,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            binding.oddOrEven.adapter = adapter
        }
        binding.oddOrEven.setSelection(getOddOrEvenId(settings.isWeekEven))
        binding.oddOrEven.onItemSelectedListener = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (binding.daysOfWeek.selectedView == view) {
            settings.firstDayOfWeek = binding.daysOfWeek.selectedItemPosition + 1
            binding.oddOrEven.setSelection(getOddOrEvenId(settings.isWeekEven))
        } else {
            settings.isWeekEven = (binding.oddOrEven.selectedItemPosition == 0)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}
