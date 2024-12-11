package com.example.spicetrack.home.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spicetrack.home.data.ListSpiceResponseItem
import com.example.spicetrack.home.ui.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SearchViewModel : ViewModel() {
    companion object {
        private const val TAG = "SearchViewModel"
    }

    private val _searchedSpices = MutableLiveData<List<ListSpiceResponseItem?>>()
    val searchedSpice: LiveData<List<ListSpiceResponseItem?>> = _searchedSpices

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchSearchedSpice(query: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().findSpice(query)
        client.enqueue(object : Callback<List<ListSpiceResponseItem>> {
            override fun onResponse(
                call: Call<List<ListSpiceResponseItem>>,
                response: Response<List<ListSpiceResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchedSpices.value = response.body() ?: emptyList()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListSpiceResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
