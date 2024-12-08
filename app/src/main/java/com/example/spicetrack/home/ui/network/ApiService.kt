package com.example.spicetrack.home.ui.network

import com.example.spicetrack.home.data.Spice
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @Multipart
    @POST("classification/infer/")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<FileUploadResponse>

    @GET("spices")
    fun getSpices(): Call<List<Spice?>?>?

}