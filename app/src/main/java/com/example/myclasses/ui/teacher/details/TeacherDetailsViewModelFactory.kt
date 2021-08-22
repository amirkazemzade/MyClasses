package com.example.myclasses.ui.teacher.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class TeacherDetailsViewModelFactory(
    private val teacherId: Long,
    private val dataSource: LessonsDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherDetailsViewModel::class.java))
            return TeacherDetailsViewModel(teacherId, dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}