package com.example.taskmanagement

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanagement.databinding.ActivityNoteAddBinding

class noteAdd : AppCompatActivity() { // corrected class name to NoteAdd

    private lateinit var binding: ActivityNoteAddBinding
    private lateinit var db: notesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = notesDatabaseHelper(this) // Initialize db

        binding.saveB.setOnClickListener{
            val title = binding.titleE.text.toString()
            val content = binding.cEditText.text.toString()
            val note = note(0, title, content)
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }
    }
}
