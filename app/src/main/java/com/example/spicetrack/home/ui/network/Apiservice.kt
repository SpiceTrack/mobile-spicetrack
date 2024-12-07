package com.example.spicetrack.home.ui.network

import okhttp3.MultipartBody
import retrofit2.http.*


interface ApiService {
    @Multipart
    @POST("classification/infer")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): FileUploadResponse

}





