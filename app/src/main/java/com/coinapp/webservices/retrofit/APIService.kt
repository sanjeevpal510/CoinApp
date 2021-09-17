package com.coinapp.webservices.retrofit

import com.coinapp.model.AssetsResponse
import com.coinapp.model.history.HistoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface APIService {

    //Get list all assets
    @GET("assets/")
    fun makeAssetsList(): Call<AssetsResponse>

    //Get history data
    @GET("assets/{coin}/history?interval=d1")
    fun makeAssetsHistory(@Path("coin") user:String): Call<HistoryResponse>


}