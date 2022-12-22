package com.example.myapplication

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadFile {
    @Headers(
        "Accept:application/json"
    )
    @Multipart
    @POST("/")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        ): Call<FENResponse>
}