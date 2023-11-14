package com.android.duckytimetable.data

import androidx.lifecycle.LiveData
//хранилище для доступа к инфе
class TimetableRepository(private val timetableDao: TimetableDao) {
    val readAllData: LiveData<List<Timetable>> = timetableDao.readAllData()
    suspend fun addTimetable(timetable:Timetable){
        timetableDao.addTimetable(timetable)
    }

}