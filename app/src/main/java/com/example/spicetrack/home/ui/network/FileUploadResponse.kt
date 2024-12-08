package com.example.spicetrack.home.ui.network

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("index")
	val index: Int? = null,

	@field:SerializedName("classification")
	val classification: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) {
	val prediction: CharSequence?
		get() = classification
}
