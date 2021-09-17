package com.coinapp.webservices.retrofit

interface APIResponseInterface {
    fun isError(errorMsg: String?, serviceCode: Int, modifiedPosition: Int)
    fun isSuccess(response: Any?, serviceCode: Int, modifiedPosition: Int)
}