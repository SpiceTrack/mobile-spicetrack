package com.example.spicetrack.home.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spicetrack.home.data.HerpsResponse
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<HerpsResponseItem>>()
    val searchResults: LiveData<List<HerpsResponseItem>> get() = _searchResults

    fun searchHerbs(query: String) {
        // Memanggil API untuk pencarian rempah berdasarkan query
        val apiService = ApiConfig.getApiService()
        val call = apiService.getHerbs(query)

        call.enqueue(object : Callback<HerpsResponse> {
            override fun onResponse(call: Call<HerpsResponse>, response: Response<HerpsResponse>) {
                if (response.isSuccessful) {
                    // Jika berhasil, update LiveData dengan data hasil pencarian
                    _searchResults.postValue(response.body()?.herpsResponse)
                } else {
                    // Jika gagal, update dengan list kosong
                    _searchResults.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<HerpsResponse>, t: Throwable) {
                // Jika gagal karena masalah jaringan atau lainnya
                _searchResults.postValue(emptyList())
            }
        })
    }
}
