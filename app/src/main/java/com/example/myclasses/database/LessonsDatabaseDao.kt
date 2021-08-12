package com.example.myclasses.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.Teacher
import com.example.myclasses.database.entities.relations.LessonWithSessions
import com.example.myclasses.database.entities.relations.SessionWithLesson

@Dao
interface LessonsDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher)

    @Update
    suspend fun updateLesson(lesson: Lesson)

    @Transaction
    @Query("SELECT * FROM lessons_table")
    fun getLessons(): LiveData<List<LessonWithSessions>>

    @Query("SELECT * FROM teacher_table")
    fun getTeachers(): LiveData<List<Teacher>>

    @Query("SELECT * FROM lessons_table WHERE lesson_name = :lessonName")
    suspend fun getLesson(lessonName: String): Lesson?

    @Query("SELECT * FROM lessons_table WHERE lessonId = :id")
    fun getLesson(id: Long): LiveData<Lesson>

    @Query("SELECT * FROM session_table WHERE lesson_id = :lessonId")
    suspend fun getSessions(lessonId: Long): List<Session>

    @Query("SELECT * FROM teacher_table WHERE teacherId = :teacherId")
    suspend fun getTeacher(teacherId: Long): Teacher?

    @Query("SELECT * FROM teacher_table WHERE teacher_name = :teacherName")
    suspend fun getTeacher(teacherName: String): Teacher?

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

    @Delete
    suspend fun deleteTeacher(teacher: Teacher)
}