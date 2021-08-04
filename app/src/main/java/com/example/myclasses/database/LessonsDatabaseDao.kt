package com.example.myclasses.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.relations.LessonWithSessions
import com.example.myclasses.database.entities.relations.SessionWithLesson

@Dao
interface LessonsDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    @Insert
    suspend fun insertSession(session: Session)

    @Transaction
    @Query("SELECT * FROM lessons_table")
    fun getLessons(): LiveData<List<LessonWithSessions>>

    @Query("SELECT * FROM lessons_table WHERE lessonName = :lessonName")
    suspend fun getLesson(lessonName: String): Lesson?

    @Query("SELECT * FROM session_table WHERE lesson_name = :lessonName")
    suspend fun getSessions(lessonName: String): List<Session>

    @Transaction
    @Query("SELECT * FROM lessons_table WHERE lessonName = :lessonName")
    fun getLessonWithSessions(lessonName: String): LiveData<LessonWithSessions>

    @Transaction
    @Query("SELECT * FROM session_table WHERE day_of_week = :day AND (week_state = :state OR week_state = 0) ORDER BY start_time ASC")
    fun getSessionsWithLessonOfDay(day: Int, state: Int): LiveData<List<SessionWithLesson>>
}