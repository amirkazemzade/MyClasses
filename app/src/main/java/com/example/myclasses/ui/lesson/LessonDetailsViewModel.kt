package com.example.myclasses.ui.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import kotlinx.coroutines.launch

class LessonDetailsViewModel(lessonName: String, private val dataSource: LessonsDatabaseDao) :
    ViewModel() {

    private val _lesson = MutableLiveData<Lesson>()
    val lesson: LiveData<Lesson>
        get() = _lesson

    private val _sessions = MutableLiveData<List<Session>>()
    val sessions: LiveData<List<Session>>
        get() = _sessions

    init {
        viewModelScope.launch {
            getLesson(lessonName)
            getSessions(lessonName)
        }
    }

    private suspend fun getLesson(lessonName: String) {
        _lesson.value = dataSource.getLesson(lessonName)
    }

    private suspend fun getSessions(lessonName: String) {
        _sessions.value = dataSource.getSessions(lessonName)
    }
}