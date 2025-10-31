package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class notesListActivity : AppCompatActivity() {
    private val notesList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listViewNotes)
        val btnAddNote = findViewById<Button>(R.id.btnAddNote)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
        listView.adapter = adapter

        // استقبال الملاحظة الجديدة من AddNoteActivity
        val newNote = intent.getStringExtra("note")
        if (!newNote.isNullOrEmpty()) {
            notesList.add(newNote)
        }

        btnAddNote.setOnClickListener {
            val intent = Intent(this, addNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
