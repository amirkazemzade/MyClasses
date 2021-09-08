package com.example.myclasses.ui.teacher.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class EditTeacherViewModelFactory(
    private val teacherId: Int,
    private val dataSource: LessonsDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTeacherViewModel::class.java))
            return EditTeacherViewModel(teacherId, dataSource) as T
        else throw IllegalArgumentException("Unknown ViewModel class")
    }
}