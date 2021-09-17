package com.coinapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.coinapp.R
import com.coinapp.activity.DetailsActivity
import com.coinapp.model.AssetsResponseData
import com.coinapp.webservices.WebserviceConstant.IMAGE_URL

class AssetsListAdapter(val context: Context, val mList: List<AssetsResponseData>) :
    RecyclerView.Adapter<AssetsListAdapter.AssetsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.assets_item_view, parent, false)
        return AssetsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: AssetsViewHolder, position: Int) {
        val assetsResponseData = mList[position]
        holder.textCoinName.text = assetsResponseData.name
        holder.textCoinRank.text = "Rank: " + assetsResponseData.rank
        holder.textCoinPrice.text = "Price: $" + assetsResponseData.priceUsd
        holder.textCoinChangePercent.text = assetsResponseData.changePercent24Hr

        Glide.with(context).asBitmap()
            .placeholder(R.drawable.ic_launcher_background)
            .load(IMAGE_URL + assetsResponseData.symbol).into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("symbol", IMAGE_URL + assetsResponseData.symbol)
            intent.putExtra("coinName", assetsResponseData.name)
            intent.putExtra("coinRank", assetsResponseData.rank)
            intent.putExtra("coinPrice", "$" + assetsResponseData.priceUsd)
            intent.putExtra("coinPercent", assetsResponseData.changePercent24Hr)
            context.startActivity(intent)
        }
    }


    class AssetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.img_Symbol)
        var textCoinName = itemView.findViewById<TextView>(R.id.tvCoinName)
        var textCoinRank = itemView.findViewById<TextView>(R.id.tvCoinRank)
        var textCoinPrice = itemView.findViewById<TextView>(R.id.tvCoinPrice)
        var textCoinChangePercent = itemView.findViewById<TextView>(R.id.tvCoinChangePercent)


    }
}

