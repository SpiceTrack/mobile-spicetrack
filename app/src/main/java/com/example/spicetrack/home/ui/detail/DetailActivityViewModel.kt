//package com.example.spicetrack.home.ui.detail
//
//import com.example.spicetrack.home.ui.network.ApiConfig
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.spicetrack.home.ui.network.DetailResponse
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class DetailActivityViewModel (private val idHerbs: Int) :
//    ViewModel() {
//    companion object {
//        private const val TAG = "DetailSpcActVM"
//    }
//
//    private val _spice = MutableLiveData<DetailResponse>()
//    val spice: LiveData<DetailResponse> = _spice
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    init {
//        fetchSpiceDetails()
//    }
//
//    private fun fetchSpiceDetails() {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getDetailSpice(idHerbs)
//        client.enqueue(object : Callback<DetailResponse> {
//            override fun onResponse(
//                call: Call<DetailResponse>,
//                response: Response<DetailResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//
//                    responseBody?.spices?.let { spices -> _spice.postValue(DetailResponse()) } ?: run {
//                        Log.d(TAG, "onFailure: ${response.message()}")
//                    }
//                } else {
//                    Log.d(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }
//}