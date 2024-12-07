package com.example.spicetrack.home.ui.network

import com.example.spicetrack.home.data.Spice
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @Multipart
    @POST("classification/infer")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
    ): FileUploadResponse

    @GET("spices")
    fun getSpices(): Call<List<Spice?>?>?

}