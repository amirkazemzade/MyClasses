package com.example.myclasses.ui.teacher.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myclasses.database.LessonsDatabaseDao

class TeacherListViewModel(dataSource: LessonsDatabaseDao) : ViewModel() {
    val teachers = dataSource.getTeachers()

    private val _navigateToNewTeacher = MutableLiveData<Boolean?>()
    val navigateToNewTeacher: LiveData<Boolean?>
        get() = _navigateToNewTeacher

    fun onAddTeacher() {
        _navigateToNewTeacher.value = true
    }

    fun onNavigateToNewTeacherDone() {
        _navigateToNewTeacher.value = null
    }
}