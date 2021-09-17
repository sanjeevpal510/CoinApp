package com.coinapp.model

import com.google.gson.annotations.SerializedName


data class AssetsResponse (

	@SerializedName("data") val data : List<AssetsResponseData>,
	@SerializedName("timestamp") val timestamp : String
)