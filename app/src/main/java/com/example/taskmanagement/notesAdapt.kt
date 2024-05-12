package com.example.taskmanagement

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class notesAdapt(private var notes: List<note>, context: Context) : RecyclerView.Adapter<notesAdapt.NoteViewHolder>() {

    private  val db: notesDatabaseHelper= notesDatabaseHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleText)
        val contentTextView: TextView = itemView.findViewById(R.id.cTView)
        val updateB: ImageView = itemView.findViewById(R.id.update)
        val deleteB: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.updateB.setOnClickListener{
            val intent =Intent(holder.itemView.context, update::class.java).apply {
                putExtra("note_id",note.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteB.setOnClickListener {
            // Display confirmation dialog before deleting
            AlertDialog.Builder(holder.itemView.context)
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete") { _, _ ->
                    db.delete(note.id)
                    refreshData(db.getNotes())
                    Toast.makeText(holder.itemView.context, "Note deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount(): Int = notes.size

    fun refreshData(newNotes: List<note>){
        notes= newNotes
        notifyDataSetChanged()
    }
}
