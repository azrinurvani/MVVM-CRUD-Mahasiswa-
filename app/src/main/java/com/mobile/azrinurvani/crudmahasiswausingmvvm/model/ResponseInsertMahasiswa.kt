package com.mobile.azrinurvani.crudmahasiswausingmvvm.model

import com.google.gson.annotations.SerializedName

data class ResponseInsertMahasiswa(

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
