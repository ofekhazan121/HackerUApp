package com.example.hackeruapp.managers


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hackeruapp.dataClasses.Note
import com.example.hackeruapp.repository.NoteRepository
import com.example.hackeruapp.retrofit.PhotoInterface
import com.example.hackeruapp.retrofit.RetrofitHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

object ImageManager {
    lateinit var placeholder: Note

    fun getPhotoFromGallery(note: Note, getImage: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        note.imgType = Note.ImageType.URI
        placeholder = note
        getImage.launch(intent)
    }

    fun imageDialog(context: Context, note: Note, getImage: ActivityResultLauncher<Intent>) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Choose Photo From")
        dialog.setMessage("Photo for the Note")
        dialog.setNeutralButton("Close") { dialogInterface: DialogInterface, i: Int -> }
        dialog.setPositiveButton("Gallery") { dialogInterface: DialogInterface, i: Int ->
            getPhotoFromGallery(note, getImage)
        }
        dialog.setNegativeButton("Api") { dialogInterface: DialogInterface, i: Int ->
            getPhotoFromApi(note,context)
        }
        dialog.show()
    }

    fun fragmentPhotoDialog(context: Context, note: Note) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Delete Photo")
        dialog.setNeutralButton("Close") { dialogInterface: DialogInterface, i: Int -> }
        dialog.setPositiveButton("Delete") { dialogInterface: DialogInterface, i: Int ->
            thread (start = true) { NoteRepository.getInstance(context).deletePhoto(note) }
        }
        dialog.show()
    }

    fun getPhotoFromApi(note:Note, context: Context) {
        val retrofit = RetrofitHelper.getInstance().create(PhotoInterface::class.java)
        GlobalScope.launch {
            val response = retrofit.getPhotos(note.item)
            if (response.body()?.hits?.size != 0){
                note.imgType = Note.ImageType.URL
                note.imgString = response.body()?.hits!![0].webformatURL
                NoteRepository.getInstance(context).updateNote(note)
            }
        }
    }

    fun onImageResultFromGallery (result: ActivityResult,context: Context) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)
                thread (start = true){
                    NoteRepository.getInstance(context).updateNoteUri(uri, placeholder)
                }
            }
        }
    }
}