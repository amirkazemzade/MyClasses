package com.example.myclasses.ui.lesson.lesson_collection

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myclasses.R
import com.example.myclasses.database.Settings
import java.util.*

class LessonCollectionViewModel(preferences: SharedPreferences, application: Application) : AndroidViewModel(application) {

    private val _settings = MutableLiveData<Settings>()
    val settings: LiveData<Settings>
        get() = _settings

    private val _days = MutableLiveData<Array<String>>()
    val days: LiveData<Array<String>>
        get() = _days

    private val _nextDayId = MutableLiveData<Int>()
    val nextDayId: LiveData<Int>
        get() = _nextDayId

    private val _todayTabId = MutableLiveData<Int>()
    val todayTabId: LiveData<Int>
        get() = _todayTabId

    private val _nextDay = MutableLiveData<String>()
    val nexDay: LiveData<String>
        get() = _nextDay

    init {
        _settings.value = Settings(preferences)

        loadDayId()

        loadTodayTabId()

        loadDays(application.resources)
        _nextDay.value = nextDayId.value?.let { days.value?.get(it) }
    }

    fun nextDay() {
        nextDayId()
        _nextDay.value = nextDayId.value?.let { days.value?.get(it) }
    }

    private fun loadDays(resource: Resources) {
        _days.value = resource.getStringArray(R.array.days_of_week_short)
    }

    private fun nextDayId() {
        _nextDayId.value = _nextDayId.value?.plus(1)?.rem(7)
    }

    private fun loadDayId() {
        _nextDayId.value = settings.value?.firstDayOfWeek?.minus(1)
    }

    private fun loadTodayTabId(){
        val calendar = Calendar.getInstance()
        val todayId = calendar.get(Calendar.DAY_OF_WEEK)
        var dif = (todayId - 1 - nextDayId.value!!)
        if (dif < 0) dif += 7
        _todayTabId.value = (dif) % 7
    }

}