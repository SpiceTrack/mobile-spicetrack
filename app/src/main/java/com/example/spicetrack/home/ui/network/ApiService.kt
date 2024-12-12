package com.example.spicetrack.home.ui.network

import com.example.spicetrack.home.data.HerpsResponse
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.data.ListSpiceResponseItem
import com.example.spicetrack.home.data.SearchResponseItem
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

    @GET("herbs")
    fun getHerbs(@Query("query") query: String): Call<HerpsResponse>

    @GET("id/{id}")
    fun getHerb(@Path("id") id: Int): Call<HerpsResponse>

    @GET
    fun getHerb(@Url url: String): Call<HerpsResponseItem>
}
