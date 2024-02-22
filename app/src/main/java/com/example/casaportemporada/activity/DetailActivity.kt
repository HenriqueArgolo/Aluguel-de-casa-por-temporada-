package com.example.casaportemporada.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.casaportemporada.Model.AdModel
import com.example.casaportemporada.R
import com.squareup.picasso.Picasso
import java.io.Serializable

class DetailActivity : AppCompatActivity() {
    private lateinit var image_details : ImageView
    private lateinit var title_detais : TextView
    private lateinit var description_details : TextView
    private lateinit var room_detis : TextView
    private lateinit var bathroom_detais : TextView
    private  lateinit var garage_details : TextView
    private  lateinit var btn_back : ImageButton
    private lateinit var ad : AdModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initComponents()

        ad = intent.extras?.getSerializable("public_ad") as AdModel
        insertData(ad)

        btn_back.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

    }


    private fun initComponents(){
        btn_back = findViewById(R.id.btn_back_main_menu)
        image_details = findViewById(R.id.image_details)
        title_detais = findViewById(R.id.title_details)
        description_details = findViewById(R.id.description_details)
        room_detis = findViewById(R.id.room_details)
        bathroom_detais = findViewById(R.id.bathroom_details)
        garage_details = findViewById(R.id.garage_deatils)
    }

    private fun insertData(ad : AdModel){
        Picasso.get().load(ad.imageUrl).into(image_details)
        title_detais.setText(ad.title)
        description_details.setText(ad.description)
        room_detis.setText(ad.room)
        bathroom_detais.setText(ad.bathroom)
        garage_details.setText(ad.garage)
    }
}