package com.example.myclasses.ui.lesson.newlesson

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

    private var _wasLessonNull = false
    private val wasLessonNull: Boolean
        get() = _wasLessonNull

    private var _currentSessions = MutableLiveData<List<Session>>()
    val currentSessions: LiveData<List<Session>>
        get() = _currentSessions

    private var sessionRemoveList = MutableLiveData<MutableList<Session>>(mutableListOf())

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
        viewModelScope.launch {
            getSessions()
        }
    }

    // function that inserts a lesson object to database
    private suspend fun insertLesson(lesson: Lesson) {
        dataSource.insertLesson(lesson)
    }

    // function that inserts a list of sessions to database
    private suspend fun insertSessions(sessions: List<Session>) {
        sessions.forEach {
            dataSource.insertSession(it)
        }
    }

    // gets lesson and its sessions by name of lesson
    fun getLessonWithSessions(name: String) {
        viewModelScope.launch {
            getLesson(name)
            getSessions()
        }
    }

    // gets lesson from database by name of it
    private suspend fun getLesson(name: String) {
        val lesson = dataSource.getLesson(name)
        if (!wasLessonNull || lesson != null) {
            _currentLesson.value = lesson
        }
        _wasLessonNull = lesson == null
    }

    // gets sessions list from database by id of lesson
    private suspend fun getSessions() {
        viewModelScope.launch {
            val lessonId = if (currentLesson.value != null) currentLesson.value?.lessonId!! else -1
            _currentSessions.value = dataSource.getSessions(lessonId)
            addNewSession()
        }
    }

    // adds a new session to list of sessions
    fun addNewSession() {
        val sessions = currentSessions.value as MutableList
        val lessonId = if (currentLesson.value != null) currentLesson.value?.lessonId!! else 0
        sessions.add(Session(0, -1, -1, -1, -1, lessonId))
        _currentSessions.value = sessions
    }

    // updates the lessonId of sessions
    private fun updateSessionsList() {
        currentSessions.value?.forEach {
            it.lessonId = currentLesson.value?.lessonId!!
        }
    }

    // function that updates the lesson in database
    private suspend fun updateLesson(lesson: Lesson) {
        dataSource.updateLesson(lesson)
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

    // click listener for save button that saves lesson to database
    fun onSaveButton(
        lessonName: String,
        des: String
    ) {
        if (currentLesson.value == null) {
            _currentLesson.value = Lesson(0, lessonName, imageName.value!!, des)
            viewModelScope.launch {
                currentLesson.value?.let {
                    insertLesson(it)
                    getLesson(it.lessonName)
                    updateSessionsList()
                }
                currentSessions.value?.forEach { session ->
                    if (session.startTime >= 0 && session.endTime >= 0 && session.weekState >= 0 && session.dayOfWeek >= 1 && session.lessonId >= 1) {
                        dataSource.insertSession(session)
                    }
                }
                sessionRemoveList.value?.forEach { session ->
                    dataSource.deleteSession(session)
                }
            }
        } else {
            currentLesson.value?.imageName = imageName.value.toString()
            currentLesson.value?.description = des
            viewModelScope.launch {
                currentLesson.value?.let { updateLesson(it) }
                currentSessions.value?.forEach { session ->
                    if (session.startTime >= 0 && session.endTime >= 0 && session.weekState >= 0 && session.dayOfWeek >= 1 && session.sessionId >= 0) {
                        dataSource.insertSession(session)
                    }
                }
                sessionRemoveList.value?.forEach { session ->
                    dataSource.deleteSession(session)
                }
            }
        }
        _navigateToLessonFragment.value = dayId
    }

    fun removeSession(session: Session) {
        val list = currentSessions.value as MutableList
        list.remove(session)
        sessionRemoveList.value?.add(session)
        _currentSessions.value = list
    }
}