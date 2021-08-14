package com.example.myclasses.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Teacher

data class LessonWithTeacher(
    @Embedded
    val lesson: Lesson,

    @Relation(
        parentColumn = "teacher_id",
        entityColumn = "teacherId"
    )
    val teacher: Teacher?
)