package com.example.spicetrack.home.ui.network

import com.example.spicetrack.home.data.ListSpiceResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {


    @Multipart
    @POST("/classification/infer")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ApiResponse>

    @GET("spices")
    fun getSpices(): Response<List<ListSpiceResponseItem>>

    @GET("search")
    fun findSpice(@Query("query") query: String): Call<List<ListSpiceResponseItem>>

    @GET("spices/{id}")
    fun getDetailSpice(@Path("id") id: Int): Call<DetailResponse>

}