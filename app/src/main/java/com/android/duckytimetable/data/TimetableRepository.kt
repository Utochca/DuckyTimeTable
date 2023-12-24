package com.android.duckytimetable.data

import androidx.lifecycle.LiveData
//хранилище для доступа к инфе
class TimetableRepository(private val timetableDao: TimetableDao) {
    val readAllData: LiveData<List<Timetable>> = timetableDao.readAllData()
    suspend fun addTimetable(timetable:Timetable){
        timetableDao.addTimetable(timetable)
    }

    suspend fun deleteTimetable(timetable: Timetable) {
        timetableDao.deleteTimetable(timetable)
    }

    suspend fun getTimetableByNameAndTime(name: String, hours: String, minutes: String): Timetable? {
        return timetableDao.getTimetableByNameAndTime(name, hours, minutes)
    }
    suspend fun updateTimetable(timetable: Timetable){
        return timetableDao.updateTimetable(timetable)
    }
}