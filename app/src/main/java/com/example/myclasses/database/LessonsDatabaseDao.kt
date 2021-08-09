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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Update
    suspend fun updateLesson(lesson: Lesson)

    @Transaction
    @Query("SELECT * FROM lessons_table")
    fun getLessons(): LiveData<List<LessonWithSessions>>

    @Query("SELECT * FROM lessons_table WHERE lesson_name = :lessonName")
    suspend fun getLesson(lessonName: String): Lesson?

    @Query("SELECT * FROM lessons_table WHERE lessonId = :id")
    fun getLesson(id: Long): LiveData<Lesson>

    @Query("SELECT * FROM session_table WHERE lesson_id = :lessonId")
    suspend fun getSessions(lessonId: Long): List<Session>

    @Transaction
    @Query("SELECT * FROM lessons_table WHERE lesson_name = :lessonName")
    fun getLessonWithSessions(lessonName: String): LiveData<LessonWithSessions>

    @Transaction
    @Query("SELECT * FROM session_table WHERE day_of_week = :day AND (week_state = :state OR week_state = 0) ORDER BY start_time ASC")
    fun getSessionsWithLessonOfDay(day: Int, state: Int): LiveData<List<SessionWithLesson>>

    @Delete
    suspend fun deleteLesson(lesson: Lesson)

    @Delete
    suspend fun deleteSession(session: Session)
}