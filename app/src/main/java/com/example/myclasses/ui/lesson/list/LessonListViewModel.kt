package com.example.myclasses.ui.lesson.list

import androidx.lifecycle.ViewModel
import com.example.myclasses.database.LessonsDatabaseDao

class LessonListViewModel(dataSource: LessonsDatabaseDao) : ViewModel() {

    val lessonsWithTeacher = dataSource.getLessonsWithTeacher()
}