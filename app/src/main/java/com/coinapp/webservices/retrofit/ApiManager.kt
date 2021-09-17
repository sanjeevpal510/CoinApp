package com.coinapp.webservices.retrofit

import android.content.Context
import com.coinapp.model.AssetsResponse
import com.coinapp.model.history.HistoryResponse
import com.coinapp.webservices.WebserviceConstant
import com.kaopiz.kprogresshud.KProgressHUD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager(val mContext: Context, val mApiResponseListener: APIResponseInterface?) {

    private var mProgressDialog: KProgressHUD? = null

    init {
        mProgressDialog = KProgressHUD(mContext)
    }

    /**
     * Make request for get history
     */
    fun makeAssetsHistoryRequest(coin:String) {
        showDialog("Loading...")
        val apiService: APIService = RetrofitClient.getClient().create(APIService::class.java)

        val call = apiService.makeAssetsHistory(coin)
        call.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>?,
                response: Response<HistoryResponse>?
            ) {
                closeDialog()
                val responseBody = response?.body()

                if (responseBody?.data != null) {
                    mApiResponseListener?.isSuccess(
                        response.body(),
                        WebserviceConstant.REQUEST_CODE_ASSETS_HISTORY,
                        0
                    )

                } else {
                    mApiResponseListener?.isError(
                        "Server Error",
                        WebserviceConstant.REQUEST_CODE_ASSETS_HISTORY,
                        0
                    )
                }

            }

            override fun onFailure(call: Call<HistoryResponse>?, t: Throwable?) {
                closeDialog()
                mApiResponseListener?.isError(
                    "Some error occur",
                    WebserviceConstant.REQUEST_CODE_ASSETS_HISTORY,
                    0
                )
            }

        })

    }

    /**
     * Make request for get assets list
     */
    fun makeAssetsListRequest() {
        showDialog("Loading...")
        val apiService: APIService = RetrofitClient.getClient().create(APIService::class.java)

        val call = apiService.makeAssetsList()
        call.enqueue(object : Callback<AssetsResponse> {
            override fun onResponse(
                call: Call<AssetsResponse>?,
                response: Response<AssetsResponse>?
            ) {
                closeDialog()
                val responseBody = response?.body()

                if (responseBody?.data != null) {
                    mApiResponseListener?.isSuccess(
                        response.body(),
                        WebserviceConstant.REQUEST_CODE_ALL_ASSETS,
                        0
                    )

                } else {
                    mApiResponseListener?.isError(
                        "Server Error",
                        WebserviceConstant.REQUEST_CODE_ALL_ASSETS,
                        0
                    )
                }

            }

            override fun onFailure(call: Call<AssetsResponse>?, t: Throwable?) {
                closeDialog()
                mApiResponseListener?.isError(
                    "Some error occur",
                    WebserviceConstant.REQUEST_CODE_ALL_ASSETS,
                    0
                )
            }

        })

    }


    private fun showDialog(message: String) {
        if (mProgressDialog != null) {
            mProgressDialog?.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            mProgressDialog?.setLabel(message)
            mProgressDialog?.setCancellable(false)
            mProgressDialog?.show()
        }
    }

    /**
     * The purpose of this method is to close the progress dialog
     */
    private fun closeDialog() {
        if (mProgressDialog != null && mProgressDialog?.isShowing as Boolean) {
            mProgressDialog?.dismiss()
        }
    }
}