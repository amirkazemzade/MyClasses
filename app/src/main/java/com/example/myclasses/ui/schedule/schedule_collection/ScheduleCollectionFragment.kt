package com.example.myclasses.ui.schedule.schedule_collection

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myclasses.R
import com.example.myclasses.databinding.FragmentScheduleBinding
import com.example.myclasses.ui.schedule.schedule_object.LessonObjectFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val NUM_OF_PAGES = 14

class LessonCollectionFragment : Fragment() {

    private lateinit var viewModel: ScheduleCollectionViewModel
    private lateinit var viewModelFactory: LessonCollectionViewModelFactory
    private lateinit var pagerAdapter: PagerAdapter

    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val arguments = LessonCollectionFragmentArgs.fromBundle(requireArguments())

        viewModelFactory =
            LessonCollectionViewModelFactory(
                arguments.currentTabId,
                getPreferences(),
                application
            )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ScheduleCollectionViewModel::class.java)

        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, _ ->
            tab.text = viewModel.nextDay.value
            nextDay()
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.setPosition(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.setPosition(tab.position)
                }
            }

        })

        viewModel.moveToDay.observe(viewLifecycleOwner, {
            it?.let { tabId ->
                val id = if (tabId == -1) viewModel.todayTabId.value!! else tabId
                binding.tabLayout.getTabAt(id)
                    ?.select() // TODO: find why selecting tab at position 1 and 2 does not work
            }
        })

        viewModel.position.observe(viewLifecycleOwner, {
            it?.let {
                backToTodayState()
            }
        })

        viewModel.navigateToNewLesson.observe(viewLifecycleOwner, { day ->
            day?.let {
                val action = LessonCollectionFragmentDirections.actionNavLessonToNewLessonFragment()
                action.dayId = day
                action.tabId = viewModel.position.value!!
                this.findNavController().navigate(action)
                doneNavigatingToNewLesson()
            }
        })

        return binding.root
    }

    inner class PagerAdapter(fragment: LessonCollectionFragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return NUM_OF_PAGES
        }

        override fun createFragment(position: Int): Fragment {
            return LessonObjectFragment(position)
        }
    }

    private fun nextDay() {
        viewModel.nextDay()
    }

    private fun getPreferences(): SharedPreferences {
        return activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
    }

    // sets back to today fab icon and visibility in different day tabs
    private fun backToTodayState() {
        when (viewModel.dayDifference.value!!) {
            in 1..Int.MAX_VALUE -> {
                binding.backToToday.visibility = View.VISIBLE
                binding.backToToday.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.ic_next_day,
                        activity?.theme
                    )
                )
            }

            in Int.MIN_VALUE..-1 -> {
                binding.backToToday.visibility = View.VISIBLE
                binding.backToToday.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.ic_previous_day,
                        activity?.theme
                    )
                )
            }

            else -> binding.backToToday.visibility = View.INVISIBLE
        }
    }

    private fun doneNavigatingToNewLesson() {
        viewModel.doneNavigatingToNewLesson()
    }
}

