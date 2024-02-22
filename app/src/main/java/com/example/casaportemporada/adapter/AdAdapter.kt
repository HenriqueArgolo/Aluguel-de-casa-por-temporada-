package com.example.casaportemporada.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.casaportemporada.Model.AdModel
import com.example.casaportemporada.R
import com.example.casaportemporada.activity.MainActivity
import com.example.casaportemporada.activity.MyAdActivity
import com.squareup.picasso.Picasso

class AdAdapter(private val adList: List<AdModel>, private val onClick: OnClick) : RecyclerView.Adapter<AdAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_model, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myAdModel = adList[position]
        Picasso.get().load(myAdModel.imageUrl).into(holder.infoImage)
        holder.infoTitle.text = myAdModel.title
        holder.infoDescription.text = myAdModel.description
        holder.infoDate.text = ""
        holder.itemView.setOnClickListener { onClick.onClickListner(myAdModel) }
    }

    override fun getItemCount(): Int {
        return adList.size
    }

    interface OnClick {
        fun onClickListner(adModel: AdModel)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val infoImage: ImageView = itemView.findViewById(R.id.ad_image_info)
        val infoDescription: TextView = itemView.findViewById(R.id.ad_text_description_info)
        val infoTitle: TextView = itemView.findViewById(R.id.ad_text_title_info)
        val infoDate: TextView = itemView.findViewById(R.id.text_date_info)
    }
}
