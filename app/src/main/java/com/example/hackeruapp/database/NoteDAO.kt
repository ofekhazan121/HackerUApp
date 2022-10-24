package com.example.hackeruapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hackeruapp.dataClasses.Note


@Dao
interface NoteDAO {

    @Insert
    fun addNote(note: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes")
    fun getNotesForService(): List<Note>


    @Query("DELETE FROM notes")
    fun deleteAll()

    @Delete
    fun deleteItem(note: Note)

    @Update
    fun updateNote(note: Note)

}