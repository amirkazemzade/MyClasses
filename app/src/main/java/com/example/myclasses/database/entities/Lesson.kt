package com.example.myclasses.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons_table")
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val lessonId: Long,

    @ColumnInfo(name = "lesson_name")
    var lessonName: String,

    @ColumnInfo(name = "image_name")
    var imageName: String,

    @ColumnInfo(name = "description")
    var description: String = ""
)

