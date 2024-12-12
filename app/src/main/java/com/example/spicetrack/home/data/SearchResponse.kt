package com.example.spicetrack.home.data

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("SearchResponse")
	val searchResponse: List<SearchResponseItem>
)

data class SearchResponseItem(

	@field:SerializedName("flavor")
	val flavor: String,

	@field:SerializedName("id_herbs")
	val idHerbs: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("subtitle")
	val subtitle: String,

	@field:SerializedName("origin")
	val origin: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("tags")
	val tags: String
)
