package com.example.myclasses.database

import android.content.SharedPreferences
import java.util.*

//TODO: remove setting class if its possible
class Settings(pref: SharedPreferences) {
    private val edit = pref.edit()
    var evenIsEven = pref.getBoolean("evenIsEven", true)
        set(value) {
            field = value
            edit.putBoolean("evenIsEven", value)
            edit.apply()
        }

    var firstDayOfWeek = pref.getInt("firstDayOfWeek", 7)
        set(value) {
            field = value
            edit.putInt("firstDayOfWeek", value)
            edit.apply()
        }

    var isWeekEven: Boolean
        get() {
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = firstDayOfWeek
            val week = calendar.get(Calendar.WEEK_OF_YEAR) % 2
            return if (evenIsEven) week == 0 else week != 0
        }
        set(value) {
            if (isWeekEven != value) {
                evenIsEven = !evenIsEven
                edit.putBoolean("evenIsEven", evenIsEven)
                edit.apply()
            }
        }

}