package com.example.hackeruapp.retrofit

import com.example.hackeruapp.dataClasses.PhotoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoInterface {

    @GET("?key=30411211-eef8699e508b60d60f0594130&")
    suspend fun getPhotos(@Query("q") query:String ): Response<PhotoApiResponse>
}