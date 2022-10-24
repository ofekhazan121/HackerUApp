package com.example.hackeruapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hackeruapp.dataClasses.Note


@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getDao(): NoteDAO
}