package com.example.myclasses.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teacher_table")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val teacherId: Int = 0,

    @ColumnInfo(name = "teacher_name")
    var name: String,

    @ColumnInfo(name = "teacher_email")
    var email: String = "",

    @ColumnInfo(name = "teacher_phone_number")
    var phoneNumber: String = "",

    @ColumnInfo(name = "teacher_address")
    var address: String = "",

    @ColumnInfo(name = "teacher_website")
    var websiteAddress: String = ""
) {
    override fun toString(): String {
        return name
    }
}