package com.android.duckytimetable.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Timetable::class], version = 1, exportSchema = false)
abstract class TimetableDatabase : RoomDatabase() {

    abstract fun timeTableDao() : TimetableDao
    companion object{
        @Volatile
        private var INSTANCE : TimetableDatabase? = null
        fun getDatabase (context: Context) : TimetableDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimetableDatabase ::class.java,
                    "TimeTableDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}