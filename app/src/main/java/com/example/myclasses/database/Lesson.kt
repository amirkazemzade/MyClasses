package com.example.myclasses.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "lessons_table")
data class Lesson(
    @PrimaryKey
    var lessonId: Long = 0L,

    @ColumnInfo(name = "lesson_name")
    var lessonName: String,

    @ColumnInfo(name = "image_name")
    var imageId: Int = 0,

    @ColumnInfo(name = "start_time")
    var startTime: Long,

    @ColumnInfo(name = "end_time")
    var endTime: Long,

    //1 for SUN, 2 for MON, 3 for TUE, 4 for WED, 5 for THU, 6 for FRI, 7 for SAT
    @ColumnInfo(name = "day_of_week")
    var dayOfWeek: Int,

    //0 for NORMAL, 1 for EVEN, 2 for ODD
    @ColumnInfo(name = "session_state")
    var sessionState: Int,

    @ColumnInfo(name = "description")
    var description: String
)

