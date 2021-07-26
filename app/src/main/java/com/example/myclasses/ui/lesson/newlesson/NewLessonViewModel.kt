package com.example.myclasses.ui.lesson.newlesson

import androidx.lifecycle.*
import com.example.myclasses.database.Lesson
import com.example.myclasses.database.LessonsDatabaseDao
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class NewLessonViewModel(
    private val dayId: Int,
    private val day: Int,
    private val dataSource: LessonsDatabaseDao
) :
    ViewModel() {

    // start time of lesson as calendar
    private var _startCalendar = MutableLiveData<Calendar>()
    val startCalendar: LiveData<Calendar>
        get() = _startCalendar

    // start time of lesson as calendar
    private var _endCalendar = MutableLiveData<Calendar>()
    val endCalendar: LiveData<Calendar>
        get() = _endCalendar

    // formatted start time of lesson
    val startTime = Transformations.map(_startCalendar) { calendar ->
        val date = Date(calendar.timeInMillis)
        DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }

    // formatted end time of lesson
    val endTime = Transformations.map(_endCalendar) { calendar ->
        val date = Date(calendar.timeInMillis)
        DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    }

    // name of selected image for lesson
    private var _imageName = MutableLiveData("ic_lesson_0")
    val imageName: LiveData<String>
        get() = _imageName

    // name list of available images
    private var _pictureList = MutableLiveData<List<String>>()
    val pictureList: LiveData<List<String>>
        get() = _pictureList

    // weather to navigate to LessonFragment or not and it's arg value
    private var _navigateToLessonFragment = MutableLiveData<Int?>()
    val navigateToLessonFragment: LiveData<Int?>
        get() = _navigateToLessonFragment

    init {
        getStartTime()
        getEndTime()
    }

    // gets default start time as calendar
    private fun getStartTime() {
        _startCalendar.value = Calendar.getInstance()
    }

    // gets default end time as calendar
    private fun getEndTime() {
        val calendar = Calendar.getInstance()
        val oneHour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        calendar.timeInMillis = calendar.timeInMillis + oneHour
        _endCalendar.value = calendar
    }

    // sets new start time to start calendar
    fun setStartTime(hourOfDay: Int, minute: Int) {
        _startCalendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        _startCalendar.value?.set(Calendar.MINUTE, minute)
        _startCalendar.value = _startCalendar.value
    }

    // sets new end time to end calendar
    fun setEndTime(hourOfDay: Int, minute: Int) {
        _endCalendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        _endCalendar.value?.set(Calendar.MINUTE, minute)
        _endCalendar.value = _endCalendar.value
    }

    // click listener for save button that saves lesson to database
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
        _navigateToLessonFragment.value = dayId
    }

    // function that inserts a lesson object to database
    suspend fun insert(lesson: Lesson) {
        dataSource.insert(lesson)
    }

    // resets value of navigation after navigation has done
    fun doneNavigatingToLessonFragment() {
        _navigateToLessonFragment.value = null
    }

    // sets list of available images for lesson image
    fun setPictureList(list: List<String>) {
        _pictureList.value = list
    }

    // sets an image for lesson
    fun setImageName(name: String) {
        _imageName.value = name
    }
}