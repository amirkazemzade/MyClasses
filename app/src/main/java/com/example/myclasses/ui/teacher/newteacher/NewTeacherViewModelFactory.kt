package com.example.myclasses.ui.teacher.newteacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class NewTeacherViewModelFactory(private val dataSource: LessonsDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTeacherViewModel::class.java))
            return NewTeacherViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}