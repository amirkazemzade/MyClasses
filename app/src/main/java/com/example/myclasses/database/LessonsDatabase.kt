package com.example.myclasses.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Lesson::class], version = 1, exportSchema = false)
abstract class LessonsDatabase : RoomDatabase() {
    abstract val lessonsDatabaseDao: LessonsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: LessonsDatabase? = null

        fun getInstance(context: Context): LessonsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LessonsDatabase::class.java,
                        "lessons_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}