package com.example.hackeruapp.fragments

import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.bumptech.glide.Glide
import com.example.hackeruapp.R
import com.example.hackeruapp.dataClasses.Note
import com.example.hackeruapp.managers.ImageManager


class NoteFragment( val note: Note ): Fragment(R.layout.note_fragment){

    override fun onResume() {
        super.onResume()
        val activity = requireActivity()
        val noteItem = activity.findViewById<TextView>(R.id.note_item)
        val noteAmount = activity.findViewById<TextView>(R.id.note_amount)
        val notePhoto = activity.findViewById<ImageView>(R.id.note_photo)
        val button = activity.findViewById<Button>( R.id.note_fragment_button)

        button.setOnClickListener {
            activity.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        noteItem.text = note.item
        noteAmount.text = note.amount.toString()
        if (note.imgType != null) {
            if (note.imgType == Note.ImageType.URI) {
               notePhoto.setImageURI(Uri.parse(note.imgString))
            }else {
                Glide.with(activity).load(note.imgString).into(notePhoto)
            }
        }

        notePhoto.setOnClickListener {
            val activity = requireActivity()
            ImageManager.fragmentPhotoDialog(activity,note)
            activity.supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}