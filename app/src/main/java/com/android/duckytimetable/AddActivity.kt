package com.android.duckytimetable

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.duckytimetable.data.Timetable
import com.android.duckytimetable.data.TimetableViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar


class AddActivity : AppCompatActivity() {
    lateinit var spinner : Spinner
    lateinit var spinnerHours :Spinner
    lateinit var spinnerMinutes :Spinner
    private var button: Button? = null
    private var button2: Button? = null
    private var flag = 0
    private var choosedYear: Int = 0
    private var choosedMonth: Int = 0
    private var choosedDay: Int = 0
    private lateinit var  mTimetableViewModel : TimetableViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
//        val service = NotificationService(applicationContext)
        spinner = findViewById(R.id.spinner) as Spinner
        spinnerHours = findViewById(R.id.spinner2) as Spinner
        spinnerMinutes = findViewById(R.id.spinner3) as Spinner
        val weekDay = arrayOf<String?>("Не выбрано","Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье")
        val myHours = arrayOf<String?>("00","01","02","03","04","05","06","07","08","09",
                                       "10", "11","12","13","14","15","16","17","18","19",
                                       "20","21","22","23")
        val myMinutes = arrayOf<String?>("00","01","02","03","04","05","06","07","08","09",
            "10","11","12","13","14","15","16","17","18","19",
            "20","21","22","23","24","25","26","27","28","29",
            "30","31","32","33","34","35","36","37","38","39",
            "40","41","42","43","44","45","46","47","48","49",
            "50","51","52","53","54","55","56","57","58","59","60")

        val arrayAdapterWeek : ArrayAdapter<Any> = ArrayAdapter<Any>(this,R.layout.item,weekDay)
        arrayAdapterWeek.setDropDownViewResource(R.layout.item)
        spinner.adapter=arrayAdapterWeek

        val arrayAdapterHours : ArrayAdapter<Any> = ArrayAdapter<Any>(this,R.layout.item,myHours)
        arrayAdapterHours.setDropDownViewResource(R.layout.item)
        spinnerHours.adapter=arrayAdapterHours

        val arrayAdapterMinutes : ArrayAdapter<Any> = ArrayAdapter<Any>(this,R.layout.item,myMinutes)
        arrayAdapterMinutes.setDropDownViewResource(R.layout.item)
        spinnerMinutes.adapter=arrayAdapterMinutes

        button = findViewById(R.id.button2)
        button?.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mTimetableViewModel = ViewModelProvider(this).get(TimetableViewModel::class.java)
        button?.setOnClickListener{
            insertDataToDatabase()
        }

        button2 = findViewById(R.id.button)
        button2?.setOnClickListener{
            showDatePickerDialog()
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
        var addweekDays = weekDays.selectedItem.toString()
        val addnewDet = newDet.text.toString()
        var newWeekDayId=0;
        if(inputCheck(addName,addHours,addMinutes,addweekDays,addnewDet)){
            Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT).show()
        }
        else{
            if(flag == 1){
                val dayOfWeek = calculateDayOfWeek(choosedYear, choosedMonth, choosedDay)
                val russianDayOfWeek = getRussianDayOfWeek(dayOfWeek)
                addweekDays = russianDayOfWeek
            }
            when(addweekDays){
                "Понедельник"->newWeekDayId=0
                "Вторник"->newWeekDayId=1
                "Среда"->newWeekDayId=2
                "Четверг"->newWeekDayId=3
                "Пятница"->newWeekDayId=4
                "Суббота"->newWeekDayId=5
                "Воскресенье"->newWeekDayId=6
            }
            val timetable = Timetable(0,addName,Integer.parseInt(addMinutes),Integer.parseInt(addHours),
                                  addweekDays,addnewDet,newWeekDayId)
            mTimetableViewModel.addTimetable(timetable)
            Toast.makeText(this,"wow! New timetable!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val scheduler = AndroidAlarmScheduler(this)
        var alarmItem: AlarmItem? = null

        if(flag == 0) {
            alarmItem = AlarmItem(
                time = createDayOfWeek(newWeekDayId + 1, addHours.toInt(), addMinutes.toInt()),
                name = addName,
                description = addnewDet
            )
            Log.d("AddActivity",createDayOfWeek(newWeekDayId+1, addHours.toInt(), addMinutes.toInt()).toString()+" "+addName+" "+addnewDet);
        }
        else{
            alarmItem = AlarmItem(
                time = createDayOfWeek2(choosedYear, choosedMonth, choosedDay, addHours.toInt(), addMinutes.toInt()),
                name = addName,
                description = addnewDet
            )
            Log.d("AddActivity",createDayOfWeek2(choosedYear, choosedMonth, choosedDay, addHours.toInt(), addMinutes.toInt()).toString()+" "+addName+" "+addnewDet);
        }

        scheduler.schedule(alarmItem)

    }
    private fun inputCheck(
        name: String, hours: String, minutes: String, //Вохвращает true если не все поля заполнены
        weekDays: String, newDet: String) : Boolean{
        if(flag == 0) {
            return ((name.isEmpty()) || (weekDays == "Не выбрано") || (newDet.isEmpty()))
        }
        else{
            return ((name.isEmpty()) || (newDet.isEmpty()))
        }
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

    fun createDayOfWeek2(year: Int, month: Int, dayOfWeek: Int, hour: Int, min: Int): LocalDateTime {
        val dt = LocalDateTime.of(year, month, dayOfWeek, hour, min, 0)

        return dt
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Здесь вы обрабатываете выбранную дату
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                // Устанавливаете выбранную дату в ваш TextView или другой элемент интерфейса
                // Например:
                button2?.text = selectedDate
                choosedYear = year
                choosedMonth = month + 1
                choosedDay = dayOfMonth
                flag = 1
                spinner.isEnabled = false
                spinner.setBackgroundColor(Color.LTGRAY);
            },
            // Установите текущую дату по умолчанию
            LocalDateTime.now().year,
            LocalDateTime.now().monthValue - 1,
            LocalDateTime.now().dayOfMonth
        )
        val cal = Calendar.getInstance()
        datePicker.datePicker.minDate = cal.timeInMillis
        datePicker.show()
    }

    fun calculateDayOfWeek(year: Int, month: Int, day: Int): DayOfWeek {
        val date = LocalDate.of(year, month, day)
        return date.dayOfWeek
    }

    fun getRussianDayOfWeek(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Понедельник"
            DayOfWeek.TUESDAY -> "Вторник"
            DayOfWeek.WEDNESDAY -> "Среда"
            DayOfWeek.THURSDAY -> "Четверг"
            DayOfWeek.FRIDAY -> "Пятница"
            DayOfWeek.SATURDAY -> "Суббота"
            DayOfWeek.SUNDAY -> "Воскресенье"
        }
    }

}