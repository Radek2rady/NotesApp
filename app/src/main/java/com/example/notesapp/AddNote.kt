package com.example.notesapp

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNote : AppCompatActivity() {
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        try {
            var bundle: Bundle = intent.extras!!
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                etTitle.setText(bundle.getString("name"))
                etDescription.setText(bundle.getString("Description"))
            }
        } catch (e: Exception) {
        }
    }

    fun buAdd(view: View) {
        var dbManager = DbManager(this)
        var values = ContentValues()
        values.put("Title", etTitle.text.toString())
        values.put("Description", etDescription.text.toString())

        if (id == 0) {
            val ID = dbManager.insert(values)
            if (ID > 0) {
                Toast.makeText(this, "note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "cannot add note", Toast.LENGTH_LONG).show()
            }
        } else {
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "note is added", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "cannot add note", Toast.LENGTH_LONG).show()
            }
        }
    }
}