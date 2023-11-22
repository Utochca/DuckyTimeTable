package com.android.duckytimetable.data

import androidx.room.Entity
import androidx.room.PrimaryKey
//класс для наших полей для базы данных(табличка показывающая содержание нашей базы данных )
@Entity(tableName = "timeTable_data")
data class Timetable (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val minutes : Int,
    val hours : Int,
    val weekDay : String,
    val details : String,
    val weekId : Int,
)