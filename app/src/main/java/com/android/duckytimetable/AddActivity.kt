package com.android.duckytimetable

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
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
        val hours : EditText = findViewById(R.id.editTextNumber)
        val minutes : EditText = findViewById(R.id.editTextNumber)
        val weekDays : Spinner = findViewById(R.id.spinner)
        val newDet : TextView = findViewById(R.id.editTextText2)

        val addName = name.text.toString()
        val addHours = hours.text
        val addMinutes = minutes.text
        val addweekDays = weekDays.selectedItem.toString()
        val addnewDet = newDet.text.toString()
        if(inputCheck(addName,addHours,addMinutes,addweekDays,addnewDet)){
            Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT).show()
        }
        else{
            val timetable = Timetable(0,addName,Integer.parseInt(addHours.toString()),Integer.parseInt(addMinutes.toString()),
                                  addweekDays,addnewDet)
            mTimetableViewModel.addTimetable(timetable)
            Toast.makeText(this,"wow! New timetable!",Toast.LENGTH_SHORT).show()
        }


    }
    private fun inputCheck(
        name: String, hours: Editable, minutes: Editable, //Вохвращает true если не все поля заполнены
        weekDays: String, newDet: String) : Boolean{
        Toast.makeText(this,"hellofromhere!",Toast.LENGTH_SHORT).show()
      return ((name.isEmpty()) || (weekDays=="Не выбрано") || (newDet.isEmpty())
              || (hours.isEmpty()) || (minutes.isEmpty()))
    }

}