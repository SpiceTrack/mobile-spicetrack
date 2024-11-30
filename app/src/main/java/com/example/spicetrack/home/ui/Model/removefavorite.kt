package com.example.spicetrack.home.ui.Model

import com.google.gson.annotations.SerializedName

data class Article(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("isFavorite")
	val isFavorite: Boolean? = null
)

data class Removefavorite(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("article")
	val article: Article? = null
)
