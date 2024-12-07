package com.example.spicetrack.home.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spicetrack.home.data.Spice
import com.example.spicetrack.home.ui.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardViewModel : ViewModel() {

    private val _spices = MutableLiveData<List<Spice>>()
    val spices: LiveData<List<Spice>> get() = _spices

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://backend-spicetrack-1036509671472.asia-southeast2.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    fun fetchSpices() {
        api.getSpices()?.enqueue(object : Callback<List<Spice>> {
            override fun onResponse(call: Call<List<Spice>>, response: Response<List<Spice>>) {
                if (response.isSuccessful) {
                    _spices.value = response.body() ?: emptyList() // Gunakan default jika null
                }
            }

            override fun onFailure(call: Call<List<Spice>>, t: Throwable) {
                // Tangani kegagalan di sini
            }
        })
    }
}
