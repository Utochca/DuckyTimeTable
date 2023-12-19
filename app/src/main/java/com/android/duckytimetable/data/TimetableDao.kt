package com.android.duckytimetable.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//методы для доступа к базе данных
@Dao
interface TimetableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE )
    fun addTimetable(timetable: Timetable)
    @Query("SELECT * FROM timeTable_data ORDER BY  weekId ASC, hours ASC, minutes ASC")
    fun readAllData(): LiveData<List<Timetable>>

    @Delete
    suspend fun deleteTimetable(timetable: Timetable)

    @Query("SELECT * FROM timeTable_data WHERE name = :name AND hours = :hours AND minutes = :minutes")
    suspend fun getTimetableByNameAndTime(name: String, hours: String, minutes: String): Timetable?
}