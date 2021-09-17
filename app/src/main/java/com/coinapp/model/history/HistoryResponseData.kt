package com.coinapp.model.history

import com.google.gson.annotations.SerializedName


data class HistoryResponseData (

	@SerializedName("priceUsd") val priceUsd : String,
	@SerializedName("time") val time : String,
	@SerializedName("date") val date : String
)