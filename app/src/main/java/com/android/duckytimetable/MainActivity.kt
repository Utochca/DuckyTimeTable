package com.android.duckytimetable

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.duckytimetable.data.TimetableViewModel


class MainActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private lateinit var mTimetableViewModel : TimetableViewModel
    private lateinit var customAdapter: CustomAdapter
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = ""
        setSupportActionBar(toolbar)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView1)
        customAdapter = CustomAdapter()
        recyclerView = findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.adapter = customAdapter
        mTimetableViewModel = ViewModelProvider(this).get(TimetableViewModel ::class.java)
        mTimetableViewModel.readAllData.observe(this, Observer { timetable ->
            customAdapter.setData(timetable)
            if(customAdapter.itemCount!=0){
                textView.visibility =  View.INVISIBLE
            }
        })
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(customAdapter, mTimetableViewModel))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        imageView.setOnClickListener {
            imageView.visibility=View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId==R.id.help){
            imageView.visibility=View.VISIBLE
            imageView.alpha= 0.4F
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}