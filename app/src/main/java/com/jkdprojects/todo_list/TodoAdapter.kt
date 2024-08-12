package com.jkdprojects.todo_list

import android.content.Context
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class TodoAdapter(private val context: Context, private val items: ArrayList<String>) : BaseAdapter() {

    private val completedItems = HashSet<Int>()

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val itemText = view.findViewById<TextView>(R.id.todo_text)
        val deleteButton = view.findViewById<Button>(R.id.delete_button)
        val completeButton = view.findViewById<Button>(R.id.complete_button)

        val text = items[position]
        if (completedItems.contains(position)) {
            val spannableString = SpannableString(text)
            val strikethroughSpan = object : StrikethroughSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.strokeWidth = 7f // Increased thickness of the strike-through line
                    ds.color = ContextCompat.getColor(context, android.R.color.holo_red_dark) // Optional: Set color of strike-through
                }
            }
            spannableString.setSpan(strikethroughSpan, 0, text.length, 0)
            itemText.text = spannableString
        } else {
            itemText.text = text
        }

        completeButton.setOnClickListener {
            if (completedItems.contains(position)) {
                completedItems.remove(position)
            } else {
                completedItems.add(position)
            }
            notifyDataSetChanged()
        }

        deleteButton.setOnClickListener {
            (context as MainActivity).deleteItem(position)
        }

        return view
    }
}
