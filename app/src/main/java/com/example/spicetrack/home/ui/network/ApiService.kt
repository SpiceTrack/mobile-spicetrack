package com.example.spicetrack.home.ui.network

import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.data.ListSpiceResponseItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @Multipart
    @POST("classification/infer/")
    fun uploadImage(
        @Part image: MultipartBody.Part,
    ): Call<ApiResponse>


    @GET("herbs")
    fun getHerps(): Call <List<HerpsResponseItem>>

    @GET("search") // Endpoint Anda, misalnya "/spices"
    fun findSpice(): Response<List<ListSpiceResponseItem>>
}