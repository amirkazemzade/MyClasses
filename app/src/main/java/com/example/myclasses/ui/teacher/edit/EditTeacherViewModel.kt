package com.example.myclasses.ui.teacher.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclasses.database.LessonsDatabaseDao
import com.example.myclasses.database.entities.Teacher
import kotlinx.coroutines.launch

class EditTeacherViewModel(
    private val teacherId: Long,
    private val dataSource: LessonsDatabaseDao
) : ViewModel() {

    private val _teacher = MutableLiveData<Teacher>()
    val teacher: LiveData<Teacher>
        get() = _teacher

    private val _isNameAvailable = MutableLiveData(true)
    val isNameAvailable: LiveData<Boolean>
        get() = _isNameAvailable

    private val _navigateUp = MutableLiveData<Boolean?>()
    val navigateUp: LiveData<Boolean?>
        get() = _navigateUp

    init {
        getTeacher()
    }

    fun getTeacher() {
        viewModelScope.launch {
            _teacher.value = dataSource.getTeacher(teacherId)
        }
    }

    private suspend fun updateTeacher() {
        teacher.value?.let { dataSource.updateTeacher(it) }
    }

    fun checkIfNameIsAvailable(name: String) {
        viewModelScope.launch {
            val teacher = dataSource.getTeacher(name)
            _isNameAvailable.value =
                if (teacher?.teacherId == teacherId) true
                else teacher == null
        }
    }

    fun onNavigateUpDone() {
        _navigateUp.value = null
    }

    fun onSaveButton(editedTeacher: Teacher) {
        viewModelScope.launch {
            teacher.value?.apply {
                name = editedTeacher.name
                email = editedTeacher.email
                phoneNumber = editedTeacher.phoneNumber
                address = editedTeacher.address
                websiteAddress = editedTeacher.websiteAddress
            }
            updateTeacher()
            _navigateUp.value = true
        }
    }
}