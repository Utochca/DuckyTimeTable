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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.duckytimetable.data.Timetable
import com.android.duckytimetable.data.TimetableDatabase
import com.android.duckytimetable.data.TimetableViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class ChangeActivity : AppCompatActivity() {
    private var button: Button? = null
    private var button2: Button? = null
    lateinit var name : EditText
    lateinit var weekDay : Spinner
    lateinit var hours :Spinner
    lateinit var minutes :Spinner
    lateinit var details : EditText
    lateinit var mTimetableViewModel: TimetableViewModel
    lateinit var db: TimetableDatabase
    private var date = ""
    private var choosedYear: Int = 0
    private var choosedMonth: Int = 0
    private var choosedDay: Int = 0
    private var flag = 0
    private var flag2 = 0
    private var afterComma = ""
    private var oldYear: Int = 0
    private var oldMonth: Int = 0
    private var oldDay: Int = 0
    private var oldName = ""
    private var oldDet = ""
    private lateinit var oldDate: LocalDateTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_activity)
        val thisweekDay = arrayOf<String?>(
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота",
            "Воскресенье"
        )
        val myHours = arrayOf<String?>(
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23"
        )
        val myMinutes = arrayOf<String?>(
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"
        )

        val changeName = intent.getStringExtra("name").toString()
        val changeHours = intent.getStringExtra("hours").toString()
        val changeMinutes = intent.getStringExtra("minutes").toString()
        val changeDetails = intent.getStringExtra("details").toString()
        var changeWeek = intent.getStringExtra("weekDay").toString()
        oldName = changeName
        oldDet = changeDetails
        name = findViewById(R.id.editTextText45)
        hours = findViewById(R.id.spinner2)
        minutes = findViewById(R.id.spinner3)
        details = findViewById(R.id.editTextText2)
        weekDay = findViewById(R.id.spinner)

        val arrayAdapterWeek: ArrayAdapter<Any> =
            ArrayAdapter<Any>(this, R.layout.item, thisweekDay)
        arrayAdapterWeek.setDropDownViewResource(R.layout.item)
        weekDay.adapter = arrayAdapterWeek

        val arrayAdapterHours: ArrayAdapter<Any> = ArrayAdapter<Any>(this, R.layout.item, myHours)
        arrayAdapterHours.setDropDownViewResource(R.layout.item)
        hours.adapter = arrayAdapterHours

        val arrayAdapterMinutes: ArrayAdapter<Any> =
            ArrayAdapter<Any>(this, R.layout.item, myMinutes)
        arrayAdapterMinutes.setDropDownViewResource(R.layout.item)
        minutes.adapter = arrayAdapterMinutes

        name.setText(changeName)
        minutes.setSelection(selectFromArray(myMinutes, changeMinutes))
        hours.setSelection(selectFromArray(myHours, changeHours))
        details.setText(changeDetails)
        if (changeWeek.indexOf(",") != -1) {
            changeWeek = changeWeek.substring(0, changeWeek.indexOf(","))
            afterComma = changeWeek.substring(changeWeek.indexOf(",") + 1)
            val dateParts = afterComma.split("/");
            oldDay = dateParts[0].toInt();
            oldMonth = dateParts[1].toInt();
            oldYear = dateParts[2].toInt();
            flag2 = 1
        }

        val addweekDays = weekDay.selectedItem.toString()
        var oldWeekDayId = 0
        when (addweekDays) {
            "Понедельник" -> oldWeekDayId = 0
            "Вторник" -> oldWeekDayId = 1
            "Среда" -> oldWeekDayId = 2
            "Четверг" -> oldWeekDayId = 3
            "Пятница" -> oldWeekDayId = 4
            "Суббота" -> oldWeekDayId = 5
            "Воскресенье" -> oldWeekDayId = 6
        }

        if (flag2 == 0){ oldDate =
            createDayOfWeek(
                oldWeekDayId + 1,
                changeHours.toInt(),
                changeMinutes.toInt())
        }
        else {
            oldDate = createDayOfWeek2(
                oldYear,
                oldMonth,
                oldDay,
                changeHours.toInt(),
                changeMinutes.toInt()
            )
        }

        weekDay.setSelection(selectFromArray(thisweekDay,changeWeek))
        //Toast.makeText(this,changeWeek, Toast.LENGTH_SHORT).show()

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

        button2 = findViewById(R.id.button)
        button2?.setOnClickListener{
            showDatePickerDialog()
        }
    }
    fun insertDataIntoScreen(changeName : String, changeMinutes : String, changeHours:String){

        mTimetableViewModel = ViewModelProvider(this).get(TimetableViewModel::class.java)
        var addweekDays = weekDay.selectedItem.toString()
        var newWeekDayId=0;
        if(flag == 1){
            val dayOfWeek = calculateDayOfWeek(choosedYear, choosedMonth, choosedDay)
            val russianDayOfWeek = getRussianDayOfWeek(dayOfWeek)
            addweekDays = russianDayOfWeek
        }
        newWeekDayId = getWeekId(addweekDays)
        if(flag == 1){
            addweekDays += date
        }
        mTimetableViewModel.viewModelScope.launch{
            val timetableToChange = mTimetableViewModel.getTimetableByNameAndTime(
                changeName,
                changeHours,
                changeMinutes
            )
            val updTimetable = timetableToChange?.let { Timetable(it.id, name.text.toString(), Integer.parseInt(minutes.selectedItem.toString()),
                                                                  Integer.parseInt(hours.selectedItem.toString()),addweekDays, details.text.toString(), newWeekDayId) }
            if(updTimetable!=null) {
                mTimetableViewModel.updateTimetable(updTimetable)
            }
            else{
                    Log.d("mu", "noo")
            }

        }
        db=TimetableDatabase.getDatabase(this)

        val scheduler = AndroidAlarmScheduler(this)
        var alarmItem: AlarmItem? = null
        var alarmItem2: AlarmItem? = null

        val addName = name.text.toString()
        val addHours = hours.selectedItem.toString()
        val addMinutes = minutes.selectedItem.toString()
        val addnewDet = details.text.toString()


        alarmItem2 = AlarmItem(
            time = oldDate,
            name = oldName,
            description = oldDet
        )

        scheduler.cancel(alarmItem2)


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

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Здесь вы обрабатываете выбранную дату
                date = "$dayOfMonth/${month + 1}/$year"
                // Устанавливаете выбранную дату в ваш TextView или другой элемент интерфейса
                // Например:
                button2?.text = date
                date = ", $dayOfMonth/${month + 1}/$year"
                choosedYear = year
                choosedMonth = month + 1
                choosedDay = dayOfMonth
                flag = 1
                weekDay.isEnabled = false
                weekDay.setBackgroundColor(Color.LTGRAY);
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