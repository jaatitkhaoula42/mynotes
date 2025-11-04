package com.example.myfirstapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class addNoteActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val rgType = findViewById<RadioGroup>(R.id.rgType)
        val cbFavorite = findViewById<CheckBox>(R.id.cbFavorite)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val selectedType = when (rgType.checkedRadioButtonId) {
                R.id.rbGoal -> NoteType.GOAL
                R.id.rbTask -> NoteType.TASK
                else -> NoteType.NOTE
            }

            val note = Note(
                title = etTitle.text.toString(),
                content = etContent.text.toString(),
                favorite = cbFavorite.isChecked,
                type = selectedType
            )

            // 🔹 حفظ الملاحظة فـ SharedPreferences
            val sharedPrefs = getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()

            val titles = sharedPrefs.getStringSet("titles", mutableSetOf())!!.toMutableSet()
            titles.add(note.title)

            editor.putStringSet("titles", titles)
            editor.putString("${note.title}_content", note.content)
            editor.putBoolean("${note.title}_fav", note.favorite)
            editor.putString("${note.title}_type", note.type.name)

            editor.apply()

            // 🔹 رجوع للقائمة
            val intent = Intent(this, notesListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
