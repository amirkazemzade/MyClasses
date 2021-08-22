package com.example.myclasses.ui.teacher.newteacher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Teacher
import kotlinx.coroutines.launch

class NewTeacherViewModel(private val dataSource: LessonsDatabaseDao) : ViewModel() {

    val teachers = dataSource.getTeachers()

    private val _currentTeacher = MutableLiveData<Teacher?>()
    val currentTeacher: LiveData<Teacher?>
        get() = _currentTeacher

    private val _navigateUp = MutableLiveData<Boolean?>()
    val navigateUp: LiveData<Boolean?>
        get() = _navigateUp

    fun getTeacher(name: String) {
        viewModelScope.launch {
            val teacher = dataSource.getTeacher(name)
            if (teacher != null || currentTeacher.value != null) {
                _currentTeacher.value = teacher
            }
        }
    }

    private suspend fun insertTeacher() {
        currentTeacher.value?.let { dataSource.insertTeacher(it) }
    }

    fun onSaveButton(teacher: Teacher) {
        viewModelScope.launch {
            if (currentTeacher.value == null) _currentTeacher.value = teacher
            insertTeacher()
            _navigateUp.value = true
        }
    }

    fun onNavigateUpDone() {
        _navigateUp.value = null
    }
}