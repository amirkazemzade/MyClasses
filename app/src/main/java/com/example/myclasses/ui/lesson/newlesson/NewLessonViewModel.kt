package com.example.myclasses.ui.lesson.newlesson

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.Lesson
import com.example.myclasses.database.LessonsDatabaseDao
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class NewLessonViewModel(private val day: Int, private val dataSource: LessonsDatabaseDao) :
    ViewModel() {

    private var _startCalendar = MutableLiveData<Calendar>()
    val startCalendar: LiveData<Calendar>
        get() = _startCalendar

    private var _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get() = _startTime

    private var _endTime = MutableLiveData<String>()
    val endTime: LiveData<String>
        get() = _endTime

    private var _endCalendar = MutableLiveData<Calendar>()
    val endCalendar: LiveData<Calendar>
        get() = _endCalendar

    private var _imageName = MutableLiveData<String>("ic_lesson_0")
    val imageName: LiveData<String>
        get() = _imageName

    private var _pictureList = MutableLiveData<List<String>>()
    val pictureList: LiveData<List<String>>
        get() = _pictureList

    private var _navigateToLessonFragment = MutableLiveData<Boolean?>()
    val navigateToLessonFragment: LiveData<Boolean?>
        get() = _navigateToLessonFragment

    init {
        getStartTime()
        getEndTime()
    }

    private fun getStartTime() {
        _startCalendar.value = Calendar.getInstance()
        val date = Date(startCalendar.value?.timeInMillis!!)
        _startTime.value = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }

    private fun getEndTime() {
        val calendar = Calendar.getInstance()
        val oneHour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        calendar.timeInMillis = calendar.timeInMillis + oneHour
        _endCalendar.value = calendar
        val date = Date(endCalendar.value?.timeInMillis!!)
        _endTime.value =
            DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }

    fun setStartTime(hourOfDay: Int, minute: Int) {
        _startCalendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        _startCalendar.value?.set(Calendar.MINUTE, minute)
        val date = Date(startCalendar.value?.timeInMillis!!)
        _startTime.value = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }

    fun setEndTime(hourOfDay: Int, minute: Int) {
        _endCalendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        _endCalendar.value?.set(Calendar.MINUTE, minute)
        val date = Date(endCalendar.value?.timeInMillis!!)
        _endTime.value =
            DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }

    fun onSaveButton(
        lessonName: String,
        weekState: Int,
        des: String
    ) {
        val startTime = startCalendar.value?.timeInMillis!!
        val endTime = endCalendar.value?.timeInMillis!!
        val imageName = imageName.value!!
        val lesson =
            Lesson(0, lessonName, imageName, startTime, endTime, day, weekState, des)
        viewModelScope.launch {
            insert(lesson)
        }
        _navigateToLessonFragment.value = true
    }

    suspend fun insert(lesson: Lesson) {
        dataSource.insert(lesson)
    }

    fun doneNavigatingToLessonFragment() {
        _navigateToLessonFragment.value = null
    }

    fun setPictureList(list: List<String>) {
        _pictureList.value = list
    }

    fun setImageName(name: String, resources: Resources) {
        _imageName.value = name
    }
}