package com.example.myclasses.ui.schedule.schedule_object

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.Settings
import com.example.myclasses.database.entities.relations.SessionWithLesson
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LessonObjectViewModel(
    private val position: Int,
    private val dataSource: LessonsDatabaseDao,
    preferences: SharedPreferences
) :
    ViewModel() {

    // an instance of saved app settings
    private var _settings = MutableLiveData<Settings>()
    val settings: LiveData<Settings>
        get() = _settings

    // date of the day
    private var _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    // weather the day is Today, Yesterday, Tomorrow or none
    private var _dayState = MutableLiveData<String>()
    val dayState: LiveData<String>
        get() = _dayState

    // the day's id in calendar
    private var _day = MutableLiveData<Int>()
    private val day: LiveData<Int>
        get() = _day

    // weather the day is in odd or even week
    private var _state = MutableLiveData<Int>()
    private val state: LiveData<Int>
        get() = _state

    // state value as string
    val stateAsString = Transformations.map(_state) { state ->
        when (state) {
            1 -> "EVEN"
            2 -> "ODD"
            else -> "EVEN"
        }
    }

    // list of lessons of this day
    private lateinit var _todaySessions: LiveData<List<SessionWithLesson>>
    val todaySessions: LiveData<List<SessionWithLesson>>
        get() = _todaySessions

    init {
        loadSettings(preferences)
        getDate()
        getDay()
        getState()

        viewModelScope.launch {
            getTodayLessons()
        }
    }

    // loads saved settings
    private fun loadSettings(preferences: SharedPreferences) {
        _settings.value = Settings(preferences)
    }

    // gets today date
    private fun getDate() {
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

        calendar.timeInMillis = calendar.timeInMillis - ((todayTabId - position) * oneDay)
        val todayDate = Date(calendar.timeInMillis)
        _date.value = DateFormat.getDateInstance(DateFormat.MEDIUM).format(todayDate)
    }

    // gets day of week id
    private fun getDay() {
        _day.value = settings.value?.firstDayOfWeek?.plus(position - 1)?.mod(7)?.plus(1)
    }

    // gets state of week
    private fun getState() {
        _state.value =
            if (position < 7) {
                if (settings.value?.isWeekEven == true) 1 else 2
            } else {
                if (settings.value?.isWeekEven == true) 2 else 1
            }
    }

    // gets today's lessons from database
    private fun getTodayLessons() {
        _todaySessions =
            day.value?.let { day ->
                state.value?.let { state ->
                    dataSource.getSessionsWithLessonOfDay(day, state)
                }
            }!!
    }
}