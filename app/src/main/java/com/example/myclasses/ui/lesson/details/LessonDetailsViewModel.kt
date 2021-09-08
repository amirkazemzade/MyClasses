package com.example.myclasses.ui.lesson.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.Teacher
import kotlinx.coroutines.launch

class LessonDetailsViewModel(
    private val lessonId: Int,
    private val dataSource: LessonsDatabaseDao
) :
    ViewModel() {

    private lateinit var _lesson: LiveData<Lesson>
    val lesson: LiveData<Lesson>
        get() = _lesson

    private val _sessions = MutableLiveData<List<Session>>()
    val sessions: LiveData<List<Session>>
        get() = _sessions

    private val _teacher = MutableLiveData<Teacher?>(null)
    val teacher: LiveData<Teacher?>
        get() = _teacher

    private val _navigateToEdit = MutableLiveData<Lesson?>()
    val navigateToEdit: LiveData<Lesson?>
        get() = _navigateToEdit

    private val _navigateToTeacherDetails = MutableLiveData<Teacher?>()
    val navigateToTeacherDetails: LiveData<Teacher?>
        get() = _navigateToTeacherDetails

    private val _navigateUp = MutableLiveData<Boolean?>()
    val navigateUp: LiveData<Boolean?>
        get() = _navigateUp

    init {
        getLessonDetails()
    }

    private fun getLessonDetails() {
        viewModelScope.launch {
            getLesson(lessonId)
        }
    }

    fun updateSessions() {
        viewModelScope.launch {
            lesson.value?.lessonId?.let { getSessions(it) }
        }
    }

    fun updateTeacher() {
        viewModelScope.launch {
            lesson.value?.teacherId?.let { getTeacher(it) }
        }
    }

    private fun getLesson(id: Int?) {
        id?.let {
            _lesson = dataSource.getLesson(it)
        }
    }

    private suspend fun getSessions(lessonId: Int) {
        _sessions.value = dataSource.getSessions(lessonId)
    }

    private suspend fun getTeacher(teacherId: Int) {
        _teacher.value = dataSource.getTeacher(teacherId)
    }

    fun onRemove() {
        viewModelScope.launch {
            lesson.value?.let { dataSource.deleteLesson(it) }
            sessions.value?.forEach { session ->
                dataSource.deleteSession(session)
            }
            _navigateUp.value = true
        }
    }

    fun onEdit() {
        _navigateToEdit.value = lesson.value
    }

    fun onTeacherClicked() {
        _navigateToTeacherDetails.value = teacher.value
    }

    fun onNavigateUpDone() {
        _navigateUp.value = null
    }

    fun onNavigateToEditDone() {
        _navigateToEdit.value = null
    }

    fun onNavigateToTeacherDetailsDone() {
        _navigateToTeacherDetails.value = null
    }

}