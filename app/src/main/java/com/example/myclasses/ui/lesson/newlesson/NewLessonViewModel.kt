package com.example.myclasses.ui.lesson.newlesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.Teacher
import kotlinx.coroutines.launch

class NewLessonViewModel(
    private val dayId: Int,
    private val day: Int,
    private val dataSource: LessonsDatabaseDao
) :
    ViewModel() {

    // list of all lessons
    val lessons = dataSource.getLessons()

    // list of all teachers
    val teachers = dataSource.getTeachers()

    // current selected lesson
    private var _currentLesson = MutableLiveData<Lesson?>()
    val currentLesson: LiveData<Lesson?>
        get() = _currentLesson

    // was previous value of current lesson null or not
    private var wasLessonNull = false

    // list of sessions of current lesson
    private var _currentSessions = MutableLiveData<List<Session>>()
    val currentSessions: LiveData<List<Session>>
        get() = _currentSessions

    // list of sessions that should be removed
    private var _sessionRemoveList = MutableLiveData<MutableList<Session>>(mutableListOf())
    val sessionRemoveList: LiveData<MutableList<Session>>
        get() = _sessionRemoveList

    // teacher of current lesson
    private val _currentTeacher = MutableLiveData<Teacher?>(null)
    val currentTeacher: LiveData<Teacher?>
        get() = _currentTeacher

    // was previous value of current teacher null or not
    private var wasTeacherNull = false

    // weather teacher menu is expanded or not
    private var _isTeacherMenuExpended = MutableLiveData(false)
    val isTeacherMenuExpended: LiveData<Boolean>
        get() = _isTeacherMenuExpended

    // name of selected image for lesson
    private var _imageName = MutableLiveData("ic_lesson_0")
    val imageName: LiveData<String>
        get() = _imageName

    // name list of available images
    private var _pictureList = MutableLiveData<List<String>>()
    val pictureList: LiveData<List<String>>
        get() = _pictureList

    private val _setAlarm = MutableLiveData<Lesson?>()
    val setAlarm: LiveData<Lesson?>
        get() = _setAlarm

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

    // function that inserts the current teacher to database
    private suspend fun insertTeacher() {
        _currentTeacher.value?.let { teacher ->
            dataSource.insertTeacher(teacher)
            getTeacher(teacher.name)
        }
    }

    // gets lesson and its sessions by name of lesson
    fun getLessonWithSessions(name: String) {
        viewModelScope.launch {
            getLesson(name)
            getSessions()
            getTeacherOfLesson()
            wasLessonNull = currentLesson.value == null
        }
    }

    // gets lesson from database by name of it
    private suspend fun getLesson(name: String) {
        val lesson = dataSource.getLesson(name)
        if (!wasLessonNull || lesson != null)
            _currentLesson.value = lesson
    }

    // gets sessions list from database by id of lesson
    private suspend fun getSessions() {
        if (!wasLessonNull || currentLesson.value != null) {
            val lessonId =
                if (currentLesson.value != null) currentLesson.value?.lessonId!! else -1
            _currentSessions.value = dataSource.getSessions(lessonId)
            addNewSession()
        }
    }

    // gets saved teacher of current lesson from database
    private suspend fun getTeacherOfLesson() {
        if (!wasLessonNull || currentLesson.value != null) {
            if (currentLesson.value != null && currentLesson.value?.teacherId != -1L) {
                currentLesson.value?.teacherId?.let { id ->
                    _currentTeacher.value = dataSource.getTeacher(id)
                }
            } else {
                _currentTeacher.value = null
            }
        }
    }

    // gets the teacher with the given name
    fun getTeacher(name: String) {
        viewModelScope.launch {
            val teacher = dataSource.getTeacher(name)
            if (!wasTeacherNull || teacher != null)
                _currentTeacher.value = teacher
            wasTeacherNull = teacher == null
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

    // updates current teacher values from given teacher
    private fun updateTeacher(teacher: Teacher) {
        _currentTeacher.value?.let { current ->
            current.name = teacher.name
            current.email = teacher.email
            current.phoneNumber = teacher.phoneNumber
            current.address = teacher.address
            current.websiteAddress = teacher.websiteAddress
        }
    }

    // removes given session from session list and adds it to remove list
    fun removeSession(session: Session) {
        val list = currentSessions.value as MutableList
        list.remove(session)
        _sessionRemoveList.value?.add(session)
        _currentSessions.value = list
    }

    fun onNavigateToLessonFragment() {
        _navigateToLessonFragment.value = dayId
    }

    // resets value of navigation after navigation has done
    fun doneNavigatingToLessonFragment() {
        _navigateToLessonFragment.value = null
    }

    fun doneSettingAlarm() {
        _setAlarm.value = null
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
        des: String,
        teacher: Teacher
    ) {
        if (currentLesson.value == null) {
            viewModelScope.launch {
                if (teacher.name != "") {
                    if (currentTeacher.value == null) _currentTeacher.value = teacher
                    else updateTeacher(teacher)
                    insertTeacher()
                } else {
                    _currentTeacher.value = null
                }
                val teacherId = _currentTeacher.value?.teacherId ?: -1L
                _currentLesson.value = Lesson(0, lessonName, imageName.value!!, des, teacherId)
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
                _sessionRemoveList.value?.forEach { session ->
                    dataSource.deleteSession(session)
                }
                getSessions()
                _setAlarm.value = currentLesson.value
            }
        } else {
            viewModelScope.launch {
                if (teacher.name != "") {
                    if (currentTeacher.value == null) _currentTeacher.value = teacher
                    else updateTeacher(teacher)
                    insertTeacher()
                } else {
                    _currentTeacher.value = null
                }
                val teacherId = _currentTeacher.value?.teacherId ?: -1L
                currentLesson.value?.teacherId = teacherId
                currentLesson.value?.imageName = imageName.value.toString()
                currentLesson.value?.description = des
                currentLesson.value?.let { updateLesson(it) }
                currentSessions.value?.forEach { session ->
                    if (session.startTime >= 0 && session.endTime >= 0 && session.weekState >= 0 && session.dayOfWeek >= 1 && session.sessionId >= 0) {
                        dataSource.insertSession(session)
                    }
                }
                _sessionRemoveList.value?.forEach { session ->
                    dataSource.deleteSession(session)
                }
                getSessions()
                _setAlarm.value = currentLesson.value
            }
        }
    }

    fun onTeacherMenuClicked() {
        _isTeacherMenuExpended.value = isTeacherMenuExpended.value?.not()
    }

    fun refreshIsTeacherMenuExpanded() {
        _isTeacherMenuExpended.value = _isTeacherMenuExpended.value
    }
}