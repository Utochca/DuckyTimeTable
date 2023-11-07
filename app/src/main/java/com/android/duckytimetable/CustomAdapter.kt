package com.android.duckytimetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.duckytimetable.data.Timetable


class CustomAdapter/*(private val list: ArrayList<Timetable>)*/ : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    private var list = emptyList<Timetable>()
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val minutes: TextView = view.findViewById(R.id.minutes)
        val hours: TextView = view.findViewById(R.id.hours)
        val weekDay: TextView = view.findViewById(R.id.weekDay)
        val details: TextView = view.findViewById(R.id.details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_adapter_layout,parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.minutes.text = item.minutes.toString()
        holder.hours.text = item.hours.toString()
        holder.weekDay.text = item.weekDay
        holder.details.text = item.details
    }

    override fun getItemCount(): Int {
        return list.count()
    }
    fun setData(timetable: List<Timetable>){
        this.list = timetable
        notifyDataSetChanged()
    }
}
