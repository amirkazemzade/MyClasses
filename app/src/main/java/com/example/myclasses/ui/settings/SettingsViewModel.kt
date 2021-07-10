package com.example.myclasses.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myclasses.database.Settings

class SettingsViewModel(preferences: SharedPreferences) : ViewModel() {

    private var _settings = MutableLiveData<Settings>()
    val settings: LiveData<Settings>
        get() = _settings

    init {
        _settings.value = Settings(preferences)
    }
}