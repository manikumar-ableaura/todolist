package com.jkdprojects.todo_list

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.jkdprojects.todo_list.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private var itemlist = ArrayList<String>()
    private val filehelper = Filehelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        itemlist = filehelper.readData(this)
        val adapter = TodoAdapter(this, itemlist)
        mainBinding.list.adapter = adapter

        mainBinding.button.setOnClickListener {
            addItem()
        }

        mainBinding.redoButton.setOnClickListener {
            redoLastDeletedItem()
        }

        mainBinding.itemname.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                addItem()
                true
            } else {
                false
            }
        }
    }

    private fun addItem() {
        val itemname = mainBinding.itemname.text.toString()
        if (itemname.isNotBlank()) {
            itemlist.add(itemname)
            mainBinding.itemname.setText("")
            filehelper.writeData(itemlist, applicationContext)
            (mainBinding.list.adapter as TodoAdapter).notifyDataSetChanged()
        }
    }

    private var deletedItems =
        mutableListOf<Pair<String, Int>>() // Store deleted items and their positions

    fun deleteItem(position: Int) {
        val item = itemlist[position]
        deletedItems.add(item to position)
        itemlist.removeAt(position)
        filehelper.writeData(itemlist, applicationContext)
        (mainBinding.list.adapter as TodoAdapter).notifyDataSetChanged()
    }

    private fun redoLastDeletedItem() {
        if (deletedItems.isNotEmpty()) {
            val (item, position) = deletedItems.removeAt(deletedItems.size - 1)
            itemlist.add(position, item)
            filehelper.writeData(itemlist, applicationContext)
            (mainBinding.list.adapter as TodoAdapter).notifyDataSetChanged()
        }
    }
}
