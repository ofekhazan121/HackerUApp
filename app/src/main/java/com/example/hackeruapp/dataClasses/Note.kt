package com.example.hackeruapp.dataClasses

import android.content.Intent
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
class Note(var item: String, var amount: Int, val creationTime: Long = System.currentTimeMillis() ){

    @PrimaryKey(autoGenerate = true)
    var id=0

    var imgString : String ?= null
    var imgType : ImageType ?= null

    override fun toString(): String {
        return "Item: $item"
    }

    enum class ImageType{
        URL,
        URI
    }
}