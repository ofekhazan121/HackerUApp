package com.example.hackeruapp.dataClasses

import com.google.gson.annotations.SerializedName

data class Hits(
    @SerializedName("id")
    var id: Int,
    @SerializedName("webformatURL")
    var webformatURL: String,
)
