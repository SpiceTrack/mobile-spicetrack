package com.example.spicetrack.home.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailActivityViewModelFactory(private val eventId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailActivityViewModel::class.java)) {
            return DetailActivityViewModel(eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
