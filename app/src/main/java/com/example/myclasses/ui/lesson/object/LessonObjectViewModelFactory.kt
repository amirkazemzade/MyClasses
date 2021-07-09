package com.example.myclasses.ui.lesson.`object`

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class LessonObjectViewModelFactory(private val positon: Int, private val dataSource: LessonsDatabaseDao) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonObjectViewModel::class.java)){
            return LessonObjectViewModel(positon, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}