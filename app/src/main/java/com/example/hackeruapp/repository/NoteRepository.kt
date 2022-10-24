package com.example.hackeruapp.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.hackeruapp.dataClasses.Note
import com.example.hackeruapp.database.NoteDatabase


class NoteRepository private constructor(context: Context){

    companion object {
        private lateinit var instance: NoteRepository

        fun getInstance(context: Context) : NoteRepository{
            if (!:: instance.isInitialized) {
                instance = NoteRepository(context)
            }
            return instance
        }
    }

    private val dao = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "notes"
    ).build().getDao()

    fun addNote(note: Note){
        dao.addNote(note)
    }

    fun getNotes(): LiveData<List<Note>> {
        return dao.getNotes()
    }

    fun deleteAll() {
        dao.deleteAll()
    }

    fun deleteItem(note: Note) {
        dao.deleteItem(note)
    }

    fun updateNote(note:Note){
        dao.updateNote(note)
    }

    fun updateNoteUri(uri: Uri,note: Note) {
        note.imgString = uri.toString()
        updateNote(note)
    }

    fun deletePhoto(note: Note) {
        note.imgString = null
        note.imgType = null
        updateNote(note)
    }

    fun getAllForService():List<Note> {
        return dao.getNotesForService()
    }
}