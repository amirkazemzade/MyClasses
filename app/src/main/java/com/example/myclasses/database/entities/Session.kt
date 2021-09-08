package com.example.myclasses.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
data class Session(
    @PrimaryKey(autoGenerate = true)
    var sessionId: Int = 0,

    @ColumnInfo(name = "start_hour")
    var startHour: Int = -1,

    @ColumnInfo(name = "start_min")
    var startMin: Int = -1,

    @ColumnInfo(name = "end_hour")
    var endHour: Int = -1,

    @ColumnInfo(name = "end_min")
    var endMin: Int = -1,

    //1 for SUN, 2 for MON, 3 for TUE, 4 for WED, 5 for THU, 6 for FRI, 7 for SAT
    @ColumnInfo(name = "day_of_week")
    var dayOfWeek: Int = -1,

    //0 for NORMAL, 1 for EVEN, 2 for ODD
    @ColumnInfo(name = "week_state")
    var weekState: Int = -1,

    @ColumnInfo(name = "lesson_id")
    var lessonId: Int
)