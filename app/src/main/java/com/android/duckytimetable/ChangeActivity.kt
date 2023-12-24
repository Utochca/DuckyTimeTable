package com.android.duckytimetable

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.duckytimetable.data.Timetable
import com.android.duckytimetable.data.TimetableDatabase
import com.android.duckytimetable.data.TimetableViewModel
import kotlinx.coroutines.launch

class ChangeActivity : AppCompatActivity() {
    private var button: Button? = null
    lateinit var name : EditText
    lateinit var weekDay : Spinner
    lateinit var hours :Spinner
    lateinit var minutes :Spinner
    lateinit var details : EditText
    lateinit var mTimetableViewModel: TimetableViewModel
    lateinit var db: TimetableDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_activity)
       val thisweekDay = arrayOf<String?>("Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье")
        val myHours = arrayOf<String?>("00","01","02","03","04","05","06","07","08","09",
            "10", "11","12","13","14","15","16","17","18","19",
            "20","21","22","23")
        val myMinutes = arrayOf<String?>("00","01","02","03","04","05","06","07","08","09",
            "10","11","12","13","14","15","16","17","18","19",
            "20","21","22","23","24","25","26","27","28","29",
            "30","31","32","33","34","35","36","37","38","39",
            "40","41","42","43","44","45","46","47","48","49",
            "50","51","52","53","54","55","56","57","58","59","60")

        val changeName = intent.getStringExtra("name").toString()
        val changeHours = intent.getStringExtra("hours").toString()
        val changeMinutes = intent.getStringExtra("minutes").toString()
        val changeDetails = intent.getStringExtra("details").toString()
        var changeWeek = intent.getStringExtra("weekDay").toString()
        name= findViewById(R.id.editTextText45)
        hours = findViewById(R.id.spinner2)
        minutes = findViewById(R.id.spinner3)
        details  = findViewById(R.id.editTextText2)
        weekDay = findViewById(R.id.spinner)

        val arrayAdapterWeek : ArrayAdapter<Any> = ArrayAdapter<Any>(this,R.layout.item,thisweekDay)
        arrayAdapterWeek.setDropDownViewResource(R.layout.item)
        weekDay.adapter=arrayAdapterWeek

        val arrayAdapterHours : ArrayAdapter<Any> = ArrayAdapter<Any>(this,R.layout.item,myHours)
        arrayAdapterHours.setDropDownViewResource(R.layout.item)
        hours.adapter=arrayAdapterHours

        val arrayAdapterMinutes : ArrayAdapter<Any> = ArrayAdapter<Any>(this,R.layout.item,myMinutes)
        arrayAdapterMinutes.setDropDownViewResource(R.layout.item)
        minutes.adapter=arrayAdapterMinutes

        name.setText(changeName)
        minutes.setSelection(selectFromArray(myMinutes,changeMinutes))
        hours.setSelection(selectFromArray(myHours,changeHours))
        details.setText(changeDetails)
        if(changeWeek.indexOf(",")!=-1){
            changeWeek = changeWeek.substring(0, changeWeek.indexOf(","))
        }

        weekDay.setSelection(selectFromArray(thisweekDay,changeWeek))
        Toast.makeText(this,changeWeek, Toast.LENGTH_SHORT).show()

        button = findViewById(R.id.button2)
        button?.setOnClickListener{
            if (name.text.toString().isEmpty() || details.text.toString().isEmpty()){
                Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT).show()
            }
            else {
                insertDataIntoScreen(changeName, changeMinutes, changeHours)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun insertDataIntoScreen(changeName : String, changeMinutes : String, changeHours:String){

        mTimetableViewModel = ViewModelProvider(this).get(TimetableViewModel::class.java)
        mTimetableViewModel.viewModelScope.launch{
            val timetableToChange = mTimetableViewModel.getTimetableByNameAndTime(
                changeName,
                changeHours,
                changeMinutes
            )
            val updTimetable = timetableToChange?.let { Timetable(it.id, name.text.toString(), Integer.parseInt(minutes.selectedItem.toString()),
                                                                  Integer.parseInt(hours.selectedItem.toString()),weekDay.selectedItem.toString(), details.text.toString(),
                                                                   getWeekId(weekDay.selectedItem.toString())) }
            if(updTimetable!=null) {
                mTimetableViewModel.updateTimetable(updTimetable)
            }
            else{
                    Log.d("mu", "noo")
            }

        }
        db=TimetableDatabase.getDatabase(this)
    }
    fun selectFromArray(array : Array<String?>,oneElement : String) : Int{
        val index = array.indexOfFirst({ it == oneElement })
        return index
    }
    fun getWeekId(weekDay :String) : Int{
        var newWeekDayId=0
        when(weekDay){
            "Понедельник"->newWeekDayId=0
            "Вторник"->newWeekDayId=1
            "Среда"->newWeekDayId=2
            "Четверг"->newWeekDayId=3
            "Пятница"->newWeekDayId=4
            "Суббота"->newWeekDayId=5
            "Воскресенье"->newWeekDayId=6
        }
        return newWeekDayId
    }
}