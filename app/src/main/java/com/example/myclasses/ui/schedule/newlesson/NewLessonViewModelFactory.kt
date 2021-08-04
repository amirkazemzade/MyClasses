package com.example.myclasses.ui.schedule.newlesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class NewLessonViewModelFactory(
    private val tabId: Int,
    private val day: Int,
    private val dataSource: LessonsDatabaseDao
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewLessonViewModel::class.java)) {
            return NewLessonViewModel(tabId, day, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}