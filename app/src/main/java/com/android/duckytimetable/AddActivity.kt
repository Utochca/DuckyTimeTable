package com.android.duckytimetable

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.duckytimetable.data.Timetable
import com.android.duckytimetable.data.TimetableViewModel

class AddActivity : AppCompatActivity() {

    private var button: Button? = null
    private lateinit var  mTimetableViewModel : TimetableViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        button = findViewById(R.id.button2)
        button?.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mTimetableViewModel = ViewModelProvider(this).get(TimetableViewModel::class.java)
        button?.setOnClickListener{
           insertDataToDatabase()
        }
    }
    private fun insertDataToDatabase(){
       val name : TextView = findViewById(R.id.editTextText)
        val hours : Spinner = findViewById(R.id.spinner3)
        val minutes : Spinner = findViewById(R.id.spinner2)
        val weekDays : Spinner = findViewById(R.id.spinner)
        val newDet : TextView = findViewById(R.id.editTextText2)

        val addName = name.text.toString()
        val addHours = hours.selectedItem.toString()
        val addMinutes = minutes.selectedItem.toString()
        val addweekDays = weekDays.selectedItem.toString()
        val addnewDet = newDet.text.toString()
        if(inputCheck(addName,addHours,addMinutes,addweekDays,addnewDet)){
            Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT).show()
        }
        else{
            val timetable = Timetable(0,addName,Integer.parseInt(addHours),Integer.parseInt(addMinutes),
                                  addweekDays,addnewDet)
            mTimetableViewModel.addTimetable(timetable)
            Toast.makeText(this,"wow! New timetable!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
    private fun inputCheck(
        name: String, hours: String, minutes: String, //Вохвращает true если не все поля заполнены
        weekDays: String, newDet: String) : Boolean{
      return ((name.isEmpty()) || (weekDays=="Не выбрано") || (newDet.isEmpty()))
    }

}