package com.example.hackeruapp.dataClasses

import com.google.gson.annotations.SerializedName

data class PhotoApiResponse (
    @SerializedName("total")
    var total: Int,
    @SerializedName("totalHits")
    var totalHits:Int,
    @SerializedName("hits")
    var hits:List<Hits>,
        )