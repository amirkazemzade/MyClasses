package com.example.myclasses.ui.lesson.lesson_collection

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LessonCollectionViewModelFactory(private val preferences: SharedPreferences, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonCollectionViewModel::class.java)) {
            return LessonCollectionViewModel(preferences, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}