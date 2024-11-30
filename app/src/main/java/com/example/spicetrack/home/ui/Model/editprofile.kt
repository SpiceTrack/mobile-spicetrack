package com.example.spicetrack.home.ui.Model

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class Editprofile(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)
