package com.example.myclasses.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myclasses.database.entities.Lesson
import com.example.myclasses.database.entities.Session

data class SessionLessonTeacher(
    @Embedded
    val session: Session,

    @Relation(
        entity = Lesson::class,
        parentColumn = "lesson_id",
        entityColumn = "lessonId"
    )
    val lessonWithTeacher: LessonWithTeacher
)