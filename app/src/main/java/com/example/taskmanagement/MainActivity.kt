package com.example.taskmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagement.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: notesDatabaseHelper
    private lateinit var notesAdapter: notesAdapt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateTextView: TextView = findViewById(R.id.DateText)
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate

        db = notesDatabaseHelper(this)
        notesAdapter = notesAdapt(db.getNotes(), this)

        binding.NotesRecView.layoutManager = LinearLayoutManager(this)
        binding.NotesRecView.adapter = notesAdapter

        binding.addbutton.setOnClickListener{
            val intent = Intent(this, noteAdd::class.java)
            startActivity(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterNotes(it) }
                return true
            }
        })
    }

    private fun filterNotes(query: String) {
        val filteredNotes = if (query.isEmpty()) {
            db.getNotes()
        } else {
            db.searchNotes("%$query%")
        }
        notesAdapter.refreshData(filteredNotes)
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getNotes())
    }
}
