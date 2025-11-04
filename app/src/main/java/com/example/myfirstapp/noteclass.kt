package com.example.myfirstapp

import java.io.Serializable

enum class NoteType : Serializable {
    NOTE, GOAL, TASK
}

data class Note(
    var title: String,
    var content: String = "",
    var favorite: Boolean = false,
    var type: NoteType = NoteType.NOTE
) : Serializable
