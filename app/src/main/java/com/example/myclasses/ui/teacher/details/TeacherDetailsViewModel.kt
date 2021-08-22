package com.example.myclasses.ui.teacher.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Teacher
import kotlinx.coroutines.launch

class TeacherDetailsViewModel(
    private val teacherId: Long,
    private val dataSource: LessonsDatabaseDao
) : ViewModel() {

    private lateinit var _teacher: LiveData<Teacher>
    val teacher: LiveData<Teacher>
        get() = _teacher

    private val _navigateToEdit = MutableLiveData<Teacher?>()
    val navigateToEdit: LiveData<Teacher?>
        get() = _navigateToEdit

    private val _navigateUp = MutableLiveData<Boolean?>()
    val navigateUp: LiveData<Boolean?>
        get() = _navigateUp

    init {
        viewModelScope.launch {
            getTeacher()
        }
    }

    private fun getTeacher() {
        _teacher = dataSource.getTeacherAsLiveData(teacherId)
    }

    fun onRemove() {
        viewModelScope.launch {
            teacher.value?.let {
                val lessons = dataSource.getLessons(teacherId)
                lessons.forEach { lesson ->
                    lesson.teacherId = -1
                    dataSource.updateLesson(lesson)
                }
                dataSource.deleteTeacher(it)
                _navigateUp.value = true
            }
        }
    }

    fun onEdit() {
        _navigateToEdit.value = teacher.value
    }

    fun onNavigateUpDone() {
        _navigateUp.value = null
    }

    fun onNavigateToEditDone() {
        _navigateToEdit.value = null
    }
}