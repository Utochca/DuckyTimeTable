package com.android.duckytimetable
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(private val adapter: CustomAdapter) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val parent: ViewGroup = viewHolder.itemView.parent as ViewGroup
        val textViews: Array<TextView> = getTextViewsFromAdapter(position, parent)
        if (direction == ItemTouchHelper.LEFT) {
            val nameTextView: String = textViews[0].text.toString()
            val minutesTextView: String = textViews[1].text.toString()
            Log.d("mu", "$nameTextView $minutesTextView")
            adapter.deleteItem(position)
        } else {
            adapter.notifyItemChanged(position)
        }
    }

    fun getTextViewsFromAdapter(position: Int, parent: ViewGroup): Array<TextView> {
        return adapter.getTextViews(position, parent)
    }
}