package com.example.myclasses.ui.lesson.`object`

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myclasses.database.Lesson
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.Settings
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LessonObjectViewModel(
    private val position: Int,
    private val dataSource: LessonsDatabaseDao,
    preferences: SharedPreferences
) :
    ViewModel() {

    private var _settings = MutableLiveData<Settings>()
    val settings: LiveData<Settings>
        get() = _settings

    private var _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    private var _dayState = MutableLiveData<String>()
    val dayState: LiveData<String>
        get() = _dayState

    private var _day = MutableLiveData<Int>()
    private val day: LiveData<Int>
        get() = _day

    private var _state = MutableLiveData<Int>()
    private val state: LiveData<Int>
        get() = _state

    private var _dayDifference = MutableLiveData<Int>()
    val dayDifference: LiveData<Int>
        get() = _dayDifference

    val stateAsString: LiveData<String>
        get() {
            return when (_state.value) {
                1 -> MutableLiveData("EVEN")
                2 -> MutableLiveData("ODD")
                else -> MutableLiveData("EVEN")
            }
        }

    private lateinit var _todayLessons: LiveData<List<Lesson>>
    val todayLessons: LiveData<List<Lesson>>
        get() = _todayLessons

    init {
        _settings.value = Settings(preferences)

        loadDate()

        _day.value = settings.value?.firstDayOfWeek?.plus(position)

        _state.value =
            if (position < 7) {
                if (settings.value?.isWeekEven == true) 1 else 2
            } else {
                if (settings.value?.isWeekEven == true) 2 else 1
            }

        getTodayLessons() // use coroutine if ui doesn't responds
    }

    private fun getTodayLessons() {
        _todayLessons =
            day.value?.let { state.value?.let { it1 -> dataSource.getTodayLessons(it, it1) } }!!
    }

    private fun loadDate() {
        val calendar = Calendar.getInstance()
        val oneDay = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)

        val todayId = calendar.get(Calendar.DAY_OF_WEEK)

        var dif = (todayId - 1 - settings.value?.firstDayOfWeek?.minus(1)!!)

        if (dif < 0) dif += 7
        val todayTabId = (dif) % 7


        _dayState.value = when (todayTabId - position) {
            1 -> "Yesterday"
            0 -> "Today"
            -1 -> "Tomorrow"
            else -> ""
        }

        _dayDifference.value = todayTabId - position

        calendar.timeInMillis = calendar.timeInMillis - ((todayTabId - position) * oneDay)
        val todayDate = Date(calendar.timeInMillis)
        _date.value = DateFormat.getDateInstance(DateFormat.MEDIUM).format(todayDate)
    }
}