package com.example.myclasses.ui.lesson.editlesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myclasses.database.LessonsDatabaseDao

class EditLessonViewModelFactory(
    private val lessonId: Long,
    private val dataSource: LessonsDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditLessonViewModel::class.java)) {
            return EditLessonViewModel(lessonId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}