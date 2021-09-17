package com.coinapp.activity

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.coinapp.R
import com.coinapp.model.history.HistoryResponse
import com.coinapp.model.history.HistoryResponseData
import com.coinapp.utils.AppUtils
import com.coinapp.webservices.WebserviceConstant
import com.coinapp.webservices.retrofit.APIResponseInterface
import com.coinapp.webservices.retrofit.ApiManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.gson.Gson
import java.util.*


class DetailsActivity : AppCompatActivity(), APIResponseInterface {
    private lateinit var chart: LineChart
    private var mApiManager: ApiManager? = null
    var date: MutableList<String> = mutableListOf()
    var amount: MutableList<Float> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val image = findViewById<ImageView>(R.id.img_CoinSymbol)
        val tvCoinName = findViewById<TextView>(R.id.textCoinName)
        val tvCoinRank = findViewById<TextView>(R.id.textCoinRank)
        val tvCoinPrice = findViewById<TextView>(R.id.textCoinPrice)
        val tvCoinPercent = findViewById<TextView>(R.id.textChangePrice)
        val back = findViewById<ImageView>(R.id.back)
        chart = findViewById<LineChart>(R.id.lineChart)
        mApiManager = ApiManager(this@DetailsActivity, this)
        back.setOnClickListener {
            finish()
        }

        val coinName=intent.getStringExtra("coinName")
        val coinRank=intent.getStringExtra("coinRank")
        val coinPrice=intent.getStringExtra("coinPrice")
        val coinPercent=intent.getStringExtra("coinPercent")
        val coinSymbol=intent.getStringExtra("symbol")


        Glide.with(this).asBitmap()
            .placeholder(R.drawable.ic_launcher_background)
            .load(coinSymbol).into(image)
        tvCoinName.text = coinName
        tvCoinRank.text = coinRank
        tvCoinPrice.text = coinPrice
        tvCoinPercent.text = coinPercent

        // Assets list  Api call
        if (AppUtils.isInternetAvailable(applicationContext)) {
            mApiManager?.makeAssetsHistoryRequest(coinName.toString().toLowerCase())
        }else{
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    override fun isError(errorMsg: String?, serviceCode: Int, modifiedPosition: Int) {
        Toast.makeText(this, errorMsg.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun isSuccess(response: Any?, serviceCode: Int, modifiedPosition: Int) {
        if (serviceCode == WebserviceConstant.REQUEST_CODE_ASSETS_HISTORY) {
            println("History Response=======>${Gson().toJson(response)}")

            val historyResponse: HistoryResponse = response as HistoryResponse
            val historyResponseData = ArrayList<HistoryResponseData>()
            historyResponseData.addAll(historyResponse.data)
            for (i in historyResponseData.indices) {
                date.add(historyResponseData[i].date!!)
                amount.add(historyResponseData[i].priceUsd.toFloat())
            }
            lineChart(amount = amount, date = date)
        }
    }

    //  line chart implement
    private fun lineChart(
        date: MutableList<String> = mutableListOf(),
        amount: MutableList<Float> = mutableListOf()
    ) {
        var xData = 0.0f
        val entries = ArrayList<Entry>()
        for (i in amount.indices) {
            entries.add(Entry(xData, amount[i]))
            xData++
            println(xData)
        }
        val dataSet = LineDataSet(entries, "")
        dataSet.color =
            this.let { ContextCompat.getColor(it, R.color.design_default_color_on_primary) }!!
        dataSet.valueTextColor =
            this.let { ContextCompat.getColor(it, R.color.design_default_color_on_primary) }!!
        dataSet.setCircleColor(this.let {
            ContextCompat.getColor(
                it,
                R.color.design_default_color_on_primary
            )
        }!!)
        dataSet.setDrawFilled(true)
        dataSet.valueTextSize = 12F
        dataSet.fillDrawable =
            this.let { ContextCompat.getDrawable(it, R.drawable.chart_gradient) }!!
        // dataSet.setDrawValues(false)
        val xAxis = chart.xAxis
        // Set the xAxis position to bottom. Default is top
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //Customizing x axis value
        try {

            val formatter: IAxisValueFormatter = object : IAxisValueFormatter {
                override fun getFormattedValue(value: Float, axis: AxisBase): String {
                    val index = value.toInt()
                    if (index < 0 || index >= date.size) {
                        return ""
                    } else {
                        return date[index]
                    }
                }
            }
            xAxis.granularity = 1f // minimum axis-step (interval) is 1
            xAxis.valueFormatter = formatter
            // Controlling right side of y axis
            val yAxisRight = chart.axisRight
            yAxisRight.isEnabled = false
            // Controlling left side of y axis
            val yAxisLeft = chart.axisLeft
            yAxisLeft.granularity = 1f
            yAxisLeft.textColor =
                this.let { ContextCompat.getColor(it, R.color.green) }!!
            yAxisLeft.textSize = 14F
            // Setting Data
            val data = LineData(dataSet)
            chart.data = data
            chart.animateX(1000)
            //refresh
            chart.invalidate()
            //hide legent
            chart.legend.isEnabled = false
            chart.getDescription().setEnabled(false)
            //   chart.setTouchEnabled(true);
            chart.xAxis.setTextColor(this.let {
                ContextCompat.getColor(
                    it,
                    R.color.green
                )
            }!!)
            chart.getXAxis().textSize = 14F
            // data start with 0 position
            chart.axisLeft.axisMinimum = 0f
            chart.axisRight.axisMinimum = 0f
            chart.setExtraOffsets(0F, 0F, 30F, 10F)
            //  xAxis.setSpaceMax(0.5f);
            xAxis.setSpaceMin(0.2f)
            // remove column grid
            chart.getXAxis().setDrawGridLines(false)
            //remove left line
            yAxisLeft.setDrawAxisLine(false)

        } catch (e: Exception) {
           e.printStackTrace()
        }

    }
}