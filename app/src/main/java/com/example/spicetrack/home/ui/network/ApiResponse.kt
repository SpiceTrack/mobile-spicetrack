package com.example.spicetrack.home.ui.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class ApiResponse(
        @field:SerializedName("index")
        val index: Int? = null,

        @field:SerializedName("classification")
        val classification: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

    )





