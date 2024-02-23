package com.example.casaportemporada.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.Model.AdModel
import com.example.casaportemporada.Model.User
import com.example.casaportemporada.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.view.View
import com.squareup.picasso.Picasso
import java.io.Serializable

class DetailActivity : AppCompatActivity() {
    private lateinit var image_details: ImageView
    private lateinit var title_detais: TextView
    private lateinit var description_details: TextView
    private lateinit var room_detis: TextView
    private lateinit var bathroom_detais: TextView
    private lateinit var garage_details: TextView
    private lateinit var btn_back: ImageButton
    private lateinit var ad: AdModel
    private var user: User = User()
    private lateinit var callBtn: Button
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initComponents()


        ad = intent.extras?.getSerializable("public_ad") as AdModel
        insertData(ad)
        id = ad.userId
        getUserById(id)
        eventClick()

    }

    private fun call() {
        val phoneNumber = user.phone
        if (phoneNumber.isNotBlank()) {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(dialIntent)
        } else {
            Toast.makeText(this, "Número de telefone não disponível", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eventClick() {
        btn_back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        callBtn.setOnClickListener {
            call()
        }
    }

    private fun getUserById(userId: String) {
        val reference: DatabaseReference = FirebaseHelper.getDatabaseReference()
            .child("users")
            .child(userId)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(User::class.java)!!
                    if (userData != null) {
                        user = userData
                    }
                } else {
                    Toast.makeText(
                        this@DetailActivity,
                        "nenhum usuário presente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun initComponents() {
        callBtn = findViewById(R.id.btn_call)
        btn_back = findViewById(R.id.btn_back_main_menu)
        image_details = findViewById(R.id.image_details)
        title_detais = findViewById(R.id.title_details)
        description_details = findViewById(R.id.description_details)
        room_detis = findViewById(R.id.room_details)
        bathroom_detais = findViewById(R.id.bathroom_details)
        garage_details = findViewById(R.id.garage_deatils)
    }

    private fun insertData(ad: AdModel) {
        Picasso.get().load(ad.imageUrl).into(image_details)
        title_detais.setText(ad.title)
        description_details.setText(ad.description)
        room_detis.setText(ad.room)
        bathroom_detais.setText(ad.bathroom)
        garage_details.setText(ad.garage)
    }


}