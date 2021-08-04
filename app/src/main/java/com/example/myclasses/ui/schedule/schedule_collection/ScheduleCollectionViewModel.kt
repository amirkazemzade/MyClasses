package com.example.myclasses.ui.schedule.schedule_collection

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myclasses.R
import com.example.myclasses.database.Settings
import java.util.*

class ScheduleCollectionViewModel(
    moveToDay: Int,
    preferences: SharedPreferences,
    application: Application
) : AndroidViewModel(application) {

    // an instance of saved app settings
    private val _settings = MutableLiveData<Settings>()
    val settings: LiveData<Settings>
        get() = _settings

    // list of day names
    private val _days = MutableLiveData<Array<String>>()
    private val days: LiveData<Array<String>>
        get() = _days

    // the day's id in calendar
    private var _day = MutableLiveData<Int>()
    private val day: LiveData<Int>
        get() = _day

    // position of today tab in tab view
    private val _todayTabId = MutableLiveData<Int>()
    val todayTabId: LiveData<Int>
        get() = _todayTabId

    // id of the next day
    private val _nextDayId = MutableLiveData<Int>()
    private val nextDayId: LiveData<Int>
        get() = _nextDayId

    // name of the next day
    private val _nextDay = MutableLiveData<String>()
    val nextDay: LiveData<String>
        get() = _nextDay

    // value of the day that should be selected
    private val _moveToDay = MutableLiveData(moveToDay)
    val moveToDay: LiveData<Int>
        get() = _moveToDay

    // position of current selected tab
    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int>
        get() = _position

    // the difference between today and the current day
    private var _dayDifference = MutableLiveData<Int>()
    val dayDifference: LiveData<Int>
        get() = _dayDifference

    // weather to navigate to NewLesson or not and it's arg value
    private var _navigateToNewLesson = MutableLiveData<Int?>()
    val navigateToNewLesson: LiveData<Int?>
        get() = _navigateToNewLesson

    init {
        loadSettings(preferences)
        getDays(application.resources)
        getDayId()
        nextDay()
        getTodayTabId()
    }

    // loads saved settings
    private fun loadSettings(preferences: SharedPreferences) {
        _settings.value = Settings(preferences)
    }

    // gets list of days
    private fun getDays(resource: Resources) {
        _days.value = resource.getStringArray(R.array.days_of_week_short)
    }

    // gets position of today tab
    private fun getTodayTabId() {
        val calendar = Calendar.getInstance()
        val todayId = calendar.get(Calendar.DAY_OF_WEEK)
        var dif = (todayId - nextDayId.value!!)
        if (dif < 0) dif += 7
        _todayTabId.value = (dif) % 7
    }

    // gets name of next day
    fun nextDay() {
        _nextDay.value = nextDayId.value?.let { days.value?.get(it) }
        getNextDayId()
    }

    // gets start id of week day in list
    private fun getDayId() {
        _nextDayId.value = settings.value?.firstDayOfWeek?.minus(1)
    }

    // gets next id of week days in list
    private fun getNextDayId() {
        _nextDayId.value = _nextDayId.value?.plus(1)?.rem(7)
    }

    // updates position of current selected tab
    fun setPosition(pos: Int) {
        _day.value = settings.value?.firstDayOfWeek?.plus(pos - 1)?.mod(7)?.plus(1)
        _dayDifference.value = todayTabId.value?.minus(pos)
        _position.value = pos
    }

    // click listener function for navigating to NewLesson
    fun onNewLesson() {
        _navigateToNewLesson.value = day.value
    }

    // resets value of navigation after navigation has done
    fun doneNavigatingToNewLesson() {
        _navigateToNewLesson.value = null
    }

    // click listener function for moving to today tab
    fun onMoveToToday() {
        _moveToDay.value = -1
    }

}