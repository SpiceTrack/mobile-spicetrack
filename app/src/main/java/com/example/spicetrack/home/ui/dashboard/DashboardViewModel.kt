package com.example.spicetrack.home.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spicetrack.home.data.ListSpiceResponseItem
import com.example.spicetrack.home.ui.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardViewModel : ViewModel() {

    private val _spices = MutableLiveData<List<ListSpiceResponseItem>>()
    val spices: LiveData<List<ListSpiceResponseItem>> get() = _spices

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://project-capstone-c242.et.r.appspot.com/herbs/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    // Menggunakan coroutine untuk memanggil API
    fun fetchSpices() {
        viewModelScope.launch {
            try {
                // Memanggil fungsi suspend secara langsung
                val response = api.getSpices()

                if (response.isSuccessful) {
                    _spices.value = response.body() ?: emptyList() // Jika data null, gunakan daftar kosong
                } else {
                    // Tangani kegagalan (misalnya tampilkan pesan error)
                }
            } catch (e: Exception) {
                // Tangani kegagalan di sini (misalnya log kesalahan atau tampilkan pesan kepada pengguna)
            }
        }
    }
}