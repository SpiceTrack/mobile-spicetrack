package com.example.spicetrack.home.ui.search

import com.example.spicetrack.home.data.HerpsResponse
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository {

    fun searchHerbs(query: String, callback: (List<HerpsResponseItem>?) -> Unit) {
        ApiConfig.getApiService().getHerbs(query).enqueue(object : Callback<HerpsResponse> {
            override fun onResponse(call: Call<HerpsResponse>, response: Response<HerpsResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.herpsResponse)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<HerpsResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
