package com.example.myclasses.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session
import com.example.myclasses.database.entities.Teacher

@Database(
    entities = [Lesson::class, Session::class, Teacher::class],
    version = 3,
    exportSchema = false
)
abstract class LessonsDatabase : RoomDatabase() {
    abstract val lessonsDatabaseDao: LessonsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: LessonsDatabase? = null

        fun getInstance(context: Context): LessonsDatabase {
            synchronized(this) {

                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LessonsDatabase::class.java,
                    "lessons_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}