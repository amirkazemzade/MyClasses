package com.example.myclasses.ui.lesson.collection

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myclasses.database.Settings
import com.example.myclasses.databinding.FragmentHomeBinding
import com.example.myclasses.ui.lesson.`object`.LessonObjectFragment
import com.google.android.material.tabs.TabLayoutMediator

private const val NUM_OF_PAGES = 14

class HomeCollectionFragment : Fragment() {

    private lateinit var viewModel: LessonCollectionViewModel
    private lateinit var viewModelFactory: LessonCollectionViewModelFactory
    private lateinit var pagerAdapter: PagerAdapter

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        viewModelFactory = LessonCollectionViewModelFactory(getPreferences(), application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LessonCollectionViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, _ ->
            tab.text = viewModel.nexDay.value
            nextDay()
        }.attach()

        binding.tabLayout.selectTab(viewModel.todayTabId.value?.let { binding.tabLayout.getTabAt(it) })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class PagerAdapter(fragment: HomeCollectionFragment) : FragmentStateAdapter(fragment) {
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
        return activity?.getPreferences(Context.MODE_PRIVATE)!!
    }
}

