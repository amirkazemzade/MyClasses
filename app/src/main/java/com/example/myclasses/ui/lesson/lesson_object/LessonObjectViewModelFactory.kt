package com.example.myclasses.ui.lesson.lesson_object

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class LessonObjectViewModelFactory(
    private val position: Int,
    private val dataSource: LessonsDatabaseDao,
    private val preferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonObjectViewModel::class.java)) {
            return LessonObjectViewModel(position, dataSource, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}