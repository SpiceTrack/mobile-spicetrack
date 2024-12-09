package com.example.spicetrack.home.ui.network

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("flavor")
	val flavor: String? = null,

	@field:SerializedName("id_herbs")
	val idHerbs: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("subtitle")
	val subtitle: String? = null,

	@field:SerializedName("origin")
	val origin: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("tags")
	val tags: String? = null
)