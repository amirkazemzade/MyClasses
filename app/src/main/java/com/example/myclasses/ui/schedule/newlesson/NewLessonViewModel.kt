package com.example.myclasses.ui.schedule.newlesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import kotlinx.coroutines.launch

class NewLessonViewModel(
    private val dayId: Int,
    private val day: Int,
    private val dataSource: LessonsDatabaseDao
) :
    ViewModel() {

    val lessons = dataSource.getLessons()

    private var _currentLesson = MutableLiveData<Lesson?>()
    val currentLesson: LiveData<Lesson?>
        get() = _currentLesson

    private var _currentSessions = MutableLiveData<List<Session>>()
    val currentSessions: LiveData<List<Session>>
        get() = _currentSessions

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
        getSessions("")
    }

    // click listener for save button that saves lesson to database
    fun onSaveButton(
        lessonName: String,
        des: String
    ) {
        currentLesson.value?.imageName = imageName.value.toString()
        currentLesson.value?.description = des
        viewModelScope.launch {
            currentLesson.value?.let { insertLesson(it) }
            currentSessions.value?.forEach { session ->
                if (session.startTime >= 0 && session.endTime >= 0 && session.weekState >= 0 && session.dayOfWeek >= 1) {
                    dataSource.insertSession(session)
                }
            }
        }
        _navigateToLessonFragment.value = dayId
    }

    // function that inserts a lesson object to database
    private suspend fun insertLesson(lesson: Lesson) {
        dataSource.insertLesson(lesson)
    }

    // function that inserts a lesson object to database
    private suspend fun insertSessions(sessions: List<Session>) {
        sessions.forEach {
            dataSource.insertSession(it)
        }
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

    fun getLesson(name: String) {
        viewModelScope.launch {
            var lesson = dataSource.getLesson(name)
            if (lesson == null) lesson = Lesson(name, imageName.value!!)
            _currentLesson.value = lesson
        }
    }

    fun getSessions(name: String) {
        viewModelScope.launch {
            _currentSessions.value = dataSource.getSessions(name)
            addNewSession()
        }
    }

    fun addNewSession() {
        val sessions = currentSessions.value as MutableList
        val name: String = currentLesson.value?.lessonName.toString()
        sessions.add(Session(0, -1, -1, -1, -1, name))
        _currentSessions.value = sessions
    }
}