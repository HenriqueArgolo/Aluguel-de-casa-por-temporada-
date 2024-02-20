package com.example.casaportemporada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.casaportemporada.Model.AdModel;
import com.example.casaportemporada.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.MyviewHolder> {
    private List<AdModel> adList = new ArrayList<>();

    public AdAdapter(List<AdModel> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_model, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        AdModel myAdModel = adList.get(position);
        Picasso.get().load(myAdModel.getImageUrl()).into(holder.infoImage);
        holder.infoTitle.setText(myAdModel.getTitle());
        holder.infoDescription.setText(myAdModel.getDescription());
        holder.infoDate.setText("");
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    static class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView infoImage;
        TextView infoDescription, infoTitle, infoDate;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            infoTitle = itemView.findViewById(R.id.ad_text_title_info);
            infoDescription = itemView.findViewById(R.id.ad_text_description_info);
            infoImage = itemView.findViewById(R.id.ad_image_info);
            infoDate = itemView.findViewById(R.id.text_date_info);
        }
    }
}
