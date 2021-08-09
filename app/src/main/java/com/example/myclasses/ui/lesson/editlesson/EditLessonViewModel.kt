package com.example.myclasses.ui.lesson.editlesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import kotlinx.coroutines.launch

class EditLessonViewModel(lessonId: Long, val dataSource: LessonsDatabaseDao) : ViewModel() {

    private lateinit var _currentLesson: LiveData<Lesson>
    val currentLesson: LiveData<Lesson>
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
    private var _navigateUp = MutableLiveData<Boolean?>()
    val navigateUp: LiveData<Boolean?>
        get() = _navigateUp

    init {
        getLesson(lessonId)
    }

    // gets Lesson object with its id
    private fun getLesson(id: Long) {
        viewModelScope.launch {
            _currentLesson = dataSource.getLesson(id)
        }
    }

    // gets Sessions of this Lesson
    fun getSessions() {
        viewModelScope.launch {
            _currentSessions.value =
                currentLesson.value?.lessonId?.let {
                    dataSource.getSessions(it)
                }
            addNewSession()
        }
    }

    // adds a new session to list of sessions
    fun addNewSession() {
        val sessions = currentSessions.value as MutableList
        val lessonId = currentLesson.value?.lessonId!!
        sessions.add(Session(0, -1, -1, -1, -1, lessonId))
        _currentSessions.value = sessions
    }

    // function that inserts a list of sessions to database
    private suspend fun insertSessions(sessions: List<Session>) {
        sessions.forEach {
            dataSource.insertSession(it)
        }
    }

    // function that updates the lesson in database
    private suspend fun updateLesson(lesson: Lesson) {
        dataSource.updateLesson(lesson)
    }

    // resets value of navigation after navigation has done
    fun doneNavigatingUp() {
        _navigateUp.value = null
    }

    // sets list of available images for lesson image
    fun setPictureList(list: List<String>) {
        _pictureList.value = list
    }

    // sets an image for lesson
    fun setImageName(name: String) {
        _imageName.value = name
    }

    // sets current lesson name to its text view
    fun setLessonName(text: String) {
        currentLesson.value?.imageName = text
    }

    // returns the condition name is already taken or not
    fun nameIsAlreadyTaken(name: String): Boolean {
        var lesson: Lesson? = null
        viewModelScope.launch {
            lesson = dataSource.getLesson(name)
        }
        lesson?.let {
            return it.lessonId != currentLesson.value?.lessonId
        }
        return false
    }

    // click listener for save button that saves lesson to database
    fun onSaveButton(
        lessonName: String,
        des: String
    ) {
        currentLesson.value?.lessonName = lessonName
        currentLesson.value?.imageName = imageName.value.toString()
        currentLesson.value?.description = des
        viewModelScope.launch {
            currentLesson.value?.let { updateLesson(it) }
            currentSessions.value?.forEach { session ->
                if (session.startTime >= 0 && session.endTime >= 0 && session.weekState >= 0 && session.dayOfWeek >= 1) {
                    dataSource.insertSession(session)
                }
            }
            _navigateUp.value = true
        }

    }
}
