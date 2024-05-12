package com.example.taskmanagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class notesDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLOUMN_TITLE = "title" // corrected the spelling here
        private const val COLOUMN_CONTENT = "content" // corrected the spelling here
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLOUMN_TITLE TEXT, $COLOUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: note) {
        val db = writableDatabase // corrected here, initialized writableDatabase
        val values = ContentValues().apply {
            put(COLOUMN_TITLE, note.title)
            put(COLOUMN_CONTENT, note.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getNotes(): List<note>{
        val notesList = mutableListOf<note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)

        while (cursor.moveToNext()){
            val id=cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title= cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_TITLE))
            val content=cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_CONTENT))

            val note= note(id,title,content)
            notesList.add(note)

        }
        cursor.close()
        db.close()
        return notesList
    }
    fun searchNotes(query: String): List<note> {
        val notesList = mutableListOf<note>()
        val db = readableDatabase
        val selection = "$COLOUMN_TITLE LIKE ?"
        val selectionArgs = arrayOf(query)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_CONTENT))
            val note = note(id, title, content)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }


    fun updateNote(note: note){
        val db= writableDatabase
        val values = ContentValues().apply {
            put(COLOUMN_TITLE,note.title)
            put(COLOUMN_CONTENT,note.content)

        }
        val where = "$COLUMN_ID= ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values,where,whereArgs)
        db.close()
    }

    fun getNote(noteId: Int): note{
        val db = readableDatabase
        val query ="SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor=db.rawQuery(query,null)
        cursor.moveToFirst()

        val id=cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_CONTENT))

        cursor.close()
        db.close()
        return note(id,title,content)


    }
    fun delete(noteId:Int){
        val  db = writableDatabase
        val where = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME,where,whereArgs)
        db.close()
    }
}
