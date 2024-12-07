package com.example.spicetrack.home.ui.dashboard

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope;
import kotlinx.coroutines.launch;
import java.util.List;

class DashboardViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    fun fetchArticles(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = ApiService.create().getArticles(apiKey)
                if (response.isSuccessful) {
                    _articles.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching articles", e)
            }
        }
    }
    fun processImageWithMLKit(image: InputImage) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Teks yang terdeteksi
                Log.d("MLKit", "Recognized Text: ${visionText.text}")
            }
            .addOnFailureListener { e ->
                Log.e("MLKit", "Text recognition failed", e)
            }
    }
}
