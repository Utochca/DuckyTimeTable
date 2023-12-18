package com.android.duckytimetable

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.duckytimetable.data.Timetable
import com.android.duckytimetable.data.TimetableViewModel
import java.time.LocalDateTime


class AddActivity : AppCompatActivity() {

    private var button: Button? = null
    private lateinit var  mTimetableViewModel : TimetableViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

//        val service = NotificationService(applicationContext)

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
        val hours : Spinner = findViewById(R.id.spinner2)
        val minutes : Spinner = findViewById(R.id.spinner3)
        val weekDays : Spinner = findViewById(R.id.spinner)
        val newDet : TextView = findViewById(R.id.editTextText2)

        val addName = name.text.toString()
        val addHours = hours.selectedItem.toString()
        val addMinutes = minutes.selectedItem.toString()
        val addweekDays = weekDays.selectedItem.toString()
        val addnewDet = newDet.text.toString()
        var newWeekDayId=0;
        if(inputCheck(addName,addHours,addMinutes,addweekDays,addnewDet)){
            Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT).show()
        }
        else{
            when(addweekDays){
                "Понедельник"->newWeekDayId=0
                "Вторник"->newWeekDayId=1
                "Среда"->newWeekDayId=2
                "Четверг"->newWeekDayId=3
                "Пятница"->newWeekDayId=4
                "Суббота"->newWeekDayId=5
                "Воскресенье"->newWeekDayId=6
            }
            val timetable = Timetable(0,addName,Integer.parseInt(addHours),Integer.parseInt(addMinutes),
                                  addweekDays,addnewDet,newWeekDayId)
            mTimetableViewModel.addTimetable(timetable)
            Toast.makeText(this,"wow! New timetable!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val scheduler = AndroidAlarmScheduler(this)
        var alarmItem: AlarmItem? = null

        alarmItem = AlarmItem(
            time = createDayOfWeek(newWeekDayId+1, addHours.toInt(), addMinutes.toInt()),
            name = addName,
            description = addnewDet
        )

        scheduler.schedule(alarmItem)

    }
    private fun inputCheck(
        name: String, hours: String, minutes: String, //Вохвращает true если не все поля заполнены
        weekDays: String, newDet: String) : Boolean{
      return ((name.isEmpty()) || (weekDays=="Не выбрано") || (newDet.isEmpty()))
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val v = currentFocus
        if (v is EditText) {
            val outRect = Rect()
            v.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                v.clearFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun createDayOfWeek(dayOfWeek: Int, hour: Int, min: Int): LocalDateTime {
        var dt = LocalDateTime.of(LocalDateTime.now().year, LocalDateTime.now().month, LocalDateTime.now().dayOfMonth, hour, min, 0)

        val daysUntilTargetDay = (dayOfWeek - dt.dayOfWeek.value + 7) % 7
        dt = dt.plusDays(daysUntilTargetDay.toLong())

        return dt
    }

}