package com.example.casaportemporada.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.casaportemporada.Adapter.AdAdapter;
import com.example.casaportemporada.Form.FormAD;
import com.example.casaportemporada.Helper.FirebaseHelper;
import com.example.casaportemporada.Model.AdModel;
import com.example.casaportemporada.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAdActivity extends AppCompatActivity {
    private  List<AdModel> ad = new ArrayList<>();
    private ImageButton back_to_main, add_ad;
    private TextView text_info;
    private RecyclerView rv_recyclerview;
    private ProgressBar progressBar;

    private AdAdapter adAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ad);
        startElements();
        eventClick();
        configRv();

    }

    @Override
    public void onStart(){
        super.onStart();
        recoveryAd();
    }

    private void startElements(){
        back_to_main = findViewById(R.id.btn_back_main);
        add_ad = findViewById(R.id.btn_add_ad);
        text_info = findViewById(R.id.loading_text);
        progressBar = findViewById(R.id.loading_ad_progressbar);
        rv_recyclerview = findViewById(R.id.rv_ad);

    }

    private void configRv(){
        rv_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        rv_recyclerview.setHasFixedSize(true);
        adAdapter = new AdAdapter(ad);
        rv_recyclerview.setAdapter(adAdapter);
    }

    private void recoveryAd(){
        DatabaseReference databaseReference = FirebaseHelper.getDatabaseReference()
                .child("ad")
                .child(FirebaseHelper.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ad.clear();
                    for (DataSnapshot snap : snapshot.getChildren()){
                        AdModel newAd = snap.getValue(AdModel.class);
                        ad.add(newAd);

                    }
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    text_info.setText("Você ainda não cadastrou anuncios");
                }
                progressBar.setVisibility(View.INVISIBLE);
                text_info.setText("");
                Collections.reverse(ad);
                adAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void eventClick(){
        back_to_main.setOnClickListener(v ->{
            startActivity(new Intent(this, MainActivity.class));
        });
        add_ad.setOnClickListener(v ->{
            startActivity(new Intent(this, FormAD.class));
        });
    }

}