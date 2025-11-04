package com.example.myfirstapp

import android.content.Context
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

    private val notesList = ArrayList<Note>()
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

        // 🔹 تحميل الملاحظات المخزنة
        loadNotes()

        // 🔹 تهيئة الأداپتر
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            notesList.map { if (it.favorite) "❤️ ${it.title}" else it.title }
        )
        listView.adapter = adapter

        // 🔹 الضغط على زر الإضافة
        btnAddNote.setOnClickListener {
            val intent = Intent(this, addNoteActivity::class.java)
            startActivity(intent)
        }

        // 🔹 عند الضغط على ملاحظة
        listView.setOnItemClickListener { _, _, position, _ ->
            val note = notesList[position]
            val intent = Intent(this, NoteDetailActivity::class.java)
            intent.putExtra("note", note)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
        refreshList()
    }

    private fun loadNotes() {
        notesList.clear()
        val sharedPrefs = getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
        val titles = sharedPrefs.getStringSet("titles", setOf()) ?: setOf()

        for (title in titles) {
            val content = sharedPrefs.getString("${title}_content", "") ?: ""
            val fav = sharedPrefs.getBoolean("${title}_fav", false)
            val type = sharedPrefs.getString("${title}_type", "NOTE") ?: "NOTE"
            notesList.add(Note(title, content, fav, NoteType.valueOf(type)))
        }
    }

    private fun refreshList() {
        adapter.clear()
        adapter.addAll(notesList.map { if (it.favorite) "❤️ ${it.title}" else it.title })
        adapter.notifyDataSetChanged()
    }
}
