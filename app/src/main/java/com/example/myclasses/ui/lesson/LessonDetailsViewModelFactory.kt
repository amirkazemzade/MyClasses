package com.example.myclasses.ui.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class LessonDetailsViewModelFactory(
    private val lessonName: String,
    private val dataSource: LessonsDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonDetailsViewModel::class.java)) {
            return LessonDetailsViewModel(lessonName, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}