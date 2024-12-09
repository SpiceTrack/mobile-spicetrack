package com.example.spicetrack.home.ui.detail

import com.example.spicetrack.home.ui.network.ApiConfig
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spicetrack.home.data.DetailSpice
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivityViewModel (private val id_herbs: Int) :
    ViewModel() {
    companion object {
        private const val TAG = "DetailSpcActVM"
    }

    private val _spice = MutableLiveData<DetailSpice>()
    val spice: LiveData<DetailSpice> = _spice

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchSpiceDetails()
    }

    private fun fetchSpiceDetails() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailSpice(id_herbs)
        client.enqueue(object : Callback<DetailSpice> {
            override fun onResponse(
                call: Call<DetailSpice>,
                response: Response<DetailSpice>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    responseBody?.spice?.let { spice -> _spice.postValue(spice) } ?: run {
                        Log.d(TAG, "onFailure: ${response.message()}")
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailSpice>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}