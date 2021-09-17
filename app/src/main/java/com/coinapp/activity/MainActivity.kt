package com.coinapp.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.coinapp.R
import com.coinapp.adapter.AssetsListAdapter
import com.coinapp.model.AssetsResponse
import com.coinapp.model.AssetsResponseData
import com.coinapp.utils.AppUtils
import com.coinapp.webservices.WebserviceConstant
import com.coinapp.webservices.retrofit.APIResponseInterface
import com.coinapp.webservices.retrofit.ApiManager
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), APIResponseInterface {
    private var mApiManager: ApiManager? = null
    private var assetsRecycler: RecyclerView? = null
    private var textEmpty: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mApiManager = ApiManager(this@MainActivity, this)
        assetsRecycler = findViewById(R.id.assetsListRecyclerView)
        textEmpty = findViewById(R.id.tvEmpty)

        // Assets list  Api call
        if (AppUtils.isInternetAvailable(applicationContext)) {
            mApiManager?.makeAssetsListRequest()
        }else{
           Toast.makeText(this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show()
        }


    }

    override fun isError(errorMsg: String?, serviceCode: Int, modifiedPosition: Int) {
        textEmpty!!.visibility = View.VISIBLE
        assetsRecycler!!.visibility = View.GONE
    }

    override fun isSuccess(response: Any?, serviceCode: Int, modifiedPosition: Int) {
        if (serviceCode == WebserviceConstant.REQUEST_CODE_ALL_ASSETS) {
            println("Assets List Response=======>${Gson().toJson(response)}")

            val assetsResponse: AssetsResponse = response as AssetsResponse
            val assetsResponseData = ArrayList<AssetsResponseData>()
            assetsResponseData.addAll(assetsResponse.data)

            assetsRecycler?.adapter = AssetsListAdapter(this@MainActivity,
                assetsResponseData
            )


        }
    }


}