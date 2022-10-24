package com.example.hackeruapp.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hackeruapp.R
import com.example.hackeruapp.dataClasses.Note
import com.example.hackeruapp.fragments.NoteFragment
import com.example.hackeruapp.managers.ImageManager
import com.example.hackeruapp.repository.NoteRepository
import kotlin.concurrent.thread

class NotesAdapter(
    var noteList: List<Note>,
    val noteFragment: (Note) -> Unit,
    val context: Context,
    val getImage: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView
        val deleteImage: ImageView
        val recycleText: TextView

        init {
            itemImage = view.findViewById(R.id.item_image)
            deleteImage = view.findViewById(R.id.delete_image)
            recycleText = view.findViewById(R.id.recycleText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recycleText.text = noteList[position].item

        holder.deleteImage.setOnClickListener {
           thread (start = true) { NoteRepository.getInstance(context).deleteItem(noteList[position]) }
        }

        holder.itemImage.setOnClickListener {
            ImageManager.imageDialog(context,noteList[position], getImage)
        }

        if (noteList[position].imgType != null) {
            if (noteList[position].imgType == Note.ImageType.URI) {
                holder.itemImage.setImageURI(Uri.parse(noteList[position].imgString))
            }else {
                Glide.with(context).load(noteList[position].imgString).into(holder.itemImage)
            }
        }

        holder.recycleText.setOnClickListener {
            noteFragment(noteList[position])
        }
    }


    override fun getItemCount(): Int {
        return noteList.size
    }
}