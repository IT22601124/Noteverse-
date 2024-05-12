package com.example.taskmanagement

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmanagement.databinding.ActivityMainBinding
import com.example.taskmanagement.databinding.ActivityUpdateBinding

class update : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: notesDatabaseHelper
    private var noteId : Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = notesDatabaseHelper(this)

        noteId= intent.getIntExtra("note_id", -1)
        if (noteId==-1){
            finish()
            return
        }

       val note=db.getNote(noteId)
        binding.updateTE.setText(note.title)
        binding.UpdateCon.setText(note.content)

        binding.updateS.setOnClickListener{
            val newT = binding.updateTE.text.toString()
            val newC = binding.UpdateCon.text.toString()
            val updatedN = note(noteId, newT , newC)
            db.updateNote(updatedN)
            finish()
            Toast.makeText(this,"Saved changing",Toast.LENGTH_SHORT).show()
        }
    }
}