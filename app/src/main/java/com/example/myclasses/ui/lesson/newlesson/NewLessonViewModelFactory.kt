package com.example.myclasses.ui.lesson.newlesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class NewLessonViewModelFactory(private val day: Int, private val dataSource: LessonsDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewLessonViewModel::class.java)) {
            return NewLessonViewModel(day, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}