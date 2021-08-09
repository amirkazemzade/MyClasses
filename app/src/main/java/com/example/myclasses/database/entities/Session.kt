package com.example.myclasses.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
data class Session(
    @PrimaryKey(autoGenerate = true)
    var sessionId: Long = 0L,

    @ColumnInfo(name = "start_time")
    var startTime: Long = 0L,

    @ColumnInfo(name = "end_time")
    var endTime: Long = 0L,

    //1 for SUN, 2 for MON, 3 for TUE, 4 for WED, 5 for THU, 6 for FRI, 7 for SAT
    @ColumnInfo(name = "day_of_week")
    var dayOfWeek: Int = 7,

    //0 for NORMAL, 1 for EVEN, 2 for ODD
    @ColumnInfo(name = "week_state")
    var weekState: Int = 0,

    @ColumnInfo(name = "lesson_id")
    var lessonId: Long
)