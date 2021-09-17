package com.coinapp.model.history

import com.google.gson.annotations.SerializedName


data class HistoryResponse (

    @SerializedName("data") val data : List<HistoryResponseData>,
    @SerializedName("timestamp") val timestamp : String
)