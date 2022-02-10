package com.example.notesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    //    @SuppressLint("Range")
    fun loadQuery(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")

        val selectionArgs = arrayOf(title)
//        val cursor = dbManager.Query(null!!, "Title like ? and name like ?", selectionArgs, "Title")     if more items
        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val Title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val Description = cursor.getString(cursor.getColumnIndexOrThrow("Description"))

                listNotes.add(Note(ID, Title, Description))

            } while (cursor.moveToNext())
        }

        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        lvNotes.adapter = myNotesAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                loadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addNote -> {
                var intent = Intent(this, AddNote::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter : BaseAdapter {
        var listNotes = ArrayList<Note>()
        var context: Context? = null

        constructor(context: Context, listNotes: ArrayList<Note>) : super() {
            this.listNotes = listNotes
            this.context = context

        }

        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myNote = listNotes[position]
            myView.tvTitle.text = myNote.noteName
            myView.tvDescription.text = myNote.noteDes

            myView.ivDelete.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.noteId.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
            }
            myView.ivEdit.setOnClickListener {
                goToUpdate(myNote)
            }
            return myView
        }
    }

    fun goToUpdate(note: Note) {
        var intent = Intent(this, AddNote::class.java)
        intent.putExtra("ID", note.noteId)
        intent.putExtra("name", note.noteName)
        intent.putExtra("Description", note.noteDes)
        startActivity(intent)
    }
}