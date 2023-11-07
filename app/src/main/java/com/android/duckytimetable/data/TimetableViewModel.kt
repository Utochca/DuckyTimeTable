package com.android.duckytimetable.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//вьюмодел для сохраненее данных во время параллельной работы
class TimetableViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Timetable>>
    private val repository: TimetableRepository
    init{
        val timetableDao = TimetableDatabase.getDatabase(application).timeTableDao()
        repository = TimetableRepository(timetableDao)
        readAllData = repository.readAllData
    }

    fun addTimetable(timetable: Timetable){
        viewModelScope.launch (Dispatchers.IO){
          repository.addTimetable(timetable)
        }
    }
}