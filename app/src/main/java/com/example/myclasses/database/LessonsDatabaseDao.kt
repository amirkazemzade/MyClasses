package com.example.myclasses.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myclasses.database.Lesson

@Dao
interface LessonsDatabaseDao {
    @Insert
    suspend fun insert(lesson: Lesson)

    @Update
    suspend fun update(lesson: Lesson)

    @Delete
    suspend fun delete(lesson: Lesson)

    @Query(value = "SELECT * FROM lessons_table WHERE lessonId = :key")
    suspend fun get(key: Long) : Lesson?

    @Query(value = "SELECT * FROM lessons_table ORDER BY lessonId DESC")
    fun getAllLessons() : LiveData<List<Lesson>>

    @Query(value = "SELECT * FROM lessons_table WHERE day_of_week = :day AND (session_state = :state OR session_state = 0)")
    fun getTodayLessons(day: Int, state: Int) : LiveData<List<Lesson>>

    @Query(value = "DELETE FROM lessons_table ")
    suspend fun clear()
}