package com.example.hackeruapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.hackeruapp.adapters.NotesAdapter
import com.example.hackeruapp.dataClasses.Note
import com.example.hackeruapp.fragments.NoteFragment
import com.example.hackeruapp.managers.ImageManager
import com.example.hackeruapp.managers.NotificationManager
import com.example.hackeruapp.repository.NoteRepository
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    var placeholder: Note? = null

    override fun onStart() {
        super.onStart()
        NoteRepository.getInstance(this).getNotes().observe(this) {
            createList(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onSubmitClick()
        val serviceIntent = Intent(this,NoteService::class.java)
        ContextCompat.startForegroundService(this,serviceIntent)
    }

    fun createNoteFragment(): (note:Note) -> Unit = {
        note ->
        val noteFragment = NoteFragment(note)
        val bundle = bundleOf("item" to note.item, "amount" to note.amount)
        noteFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, noteFragment).commit()
    }

    fun onSubmitClick() {
        val button: Button = findViewById(R.id.submit_button)

        button.setOnClickListener {
            addNote()
        }
    }

    fun addNote() {
        val editItem = findViewById<EditText>(R.id.edit_item).text.toString()
        val editAmount = findViewById<EditText>(R.id.edit_amount).text.toString().toInt()

        thread (start = true) {
            NoteRepository.getInstance(this).addNote(Note(editItem,editAmount))
        }
    }

    fun createList(noteList: List<Note>) {
        val listView = findViewById<RecyclerView>(R.id.main_recycler)
        val adapter = NotesAdapter(noteList, createNoteFragment() ,this,getImage)

        listView.adapter = adapter
    }

    val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        ImageManager.onImageResultFromGallery(result,this)
    }
}