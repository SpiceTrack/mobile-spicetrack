package com.example.spicetrack.home.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spicetrack.home.data.HerpsResponseItem
import com.example.spicetrack.home.ui.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

//    private val _spices = MutableLiveData<List<ListSpiceResponse>>()
//    val spices: LiveData<List<ListSpiceResponse>> get() = _spices
//
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl("https://backend-spicetrack-1036509671472.asia-southeast2.run.app/herbs")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    private val api = retrofit.create(ApiService::class.java)
//
//    suspend fun fetchSpices() {
//        api.getSpices()?.enqueue(object : Callback<List<ListSpiceResponse>> {
//            override fun onResponse(call: Call<List<ListSpiceResponse>>, response: Response<List<Spice>>) {
//                if (response.isSuccessful) {
//                    _spices.value = response.body() ?: emptyList() // Gunakan default jika null
//                }
//            }
//
//            override fun onFailure(call: Call<List<ListSpiceResponse>>, t: Throwable) {
//                // Tangani kegagalan di sini
//            }
//        })
//    }


    private val _herps = MutableLiveData<List<HerpsResponseItem>>()
    val herps: LiveData<List<HerpsResponseItem>> = _herps


    init {
        findHerps()
    }

    private fun findHerps() {
        val client = ApiConfig.getApiService().getHerps()
        client.enqueue(object : Callback<List<HerpsResponseItem>> {
            override fun onResponse(call: Call<List<HerpsResponseItem>>, response: Response<List<HerpsResponseItem>>) {
                if (response.isSuccessful) {
                    _herps.value = response.body()?.subList(0,30)
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        setHerpsData(responseBody.subList(0,20))
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<HerpsResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DashboardViewModel"
    }

}
