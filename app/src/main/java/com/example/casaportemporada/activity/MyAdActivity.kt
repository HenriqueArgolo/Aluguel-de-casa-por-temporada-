package com.example.casaportemporada.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.Model.AdModel
import com.example.casaportemporada.R
import com.example.casaportemporada.adapter.AdAdapter
import com.example.casaportemporada.form.FormAD
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import com.tsuryo.swipeablerv.SwipeableRecyclerView


class MyAdActivity : AppCompatActivity(), AdAdapter.OnClick {
    private var ad: MutableList<AdModel> = ArrayList()
    private lateinit var backToMain: ImageButton
    private lateinit var addAd: ImageButton
    private lateinit var textInfo: TextView
    private lateinit var rvRecyclerView: SwipeableRecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var adAdapter: AdAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ad)
        startElements()
        eventClick()
        configRv()
    }

    override fun onStart() {
        super.onStart()
        recoveryAd()
    }

    private fun startElements() {
        backToMain = findViewById(R.id.back_to_main)
        addAd = findViewById(R.id.btn_add_ad)
        textInfo = findViewById(R.id.loading_text)
        progressBar = findViewById(R.id.loading_ad_progressbar)
        rvRecyclerView = findViewById(R.id.rv_ad)
    }

    private fun configRv() {
        rvRecyclerView.layoutManager = LinearLayoutManager(this)
        rvRecyclerView.setHasFixedSize(true)
        adAdapter = AdAdapter(ad, this)
        rvRecyclerView.adapter = adAdapter

        rvRecyclerView.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {

            }

            override fun onSwipedRight(position: Int) {
                alertDialog(position);
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun alertDialog(pos : Int){
        val alert : AlertDialog.Builder = AlertDialog.Builder(this);
        alert.setTitle("Deletar anuncio")
        alert.setMessage("Você deseja deletar esse anuncio?")
        alert.setCancelable(false)
        alert.setPositiveButton("Sim"){dialog, _ ->
            ad[pos].delete()
            adAdapter.notifyItemRemoved(pos)
        }
        alert.setNegativeButton("Não"){dialog, _ ->
            dialog.dismiss()
            adAdapter.notifyDataSetChanged();
        }
        val dialog : AlertDialog = alert.create()
        dialog.show()
    }

    private fun recoveryAd() {
        val databaseReference: DatabaseReference = FirebaseHelper.getDatabaseReference()
                .child("ad")
                .child(FirebaseHelper.getUserId())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ad.clear()
                    for (snap in snapshot.children) {
                        val newAd = snap.getValue(AdModel::class.java)
                        newAd?.let { ad.add(it) }
                    }
                } else {
                    progressBar.visibility = View.INVISIBLE
                    textInfo.text = "Você ainda não cadastrou anuncios"
                }
                progressBar.visibility = View.INVISIBLE
                textInfo.text = ""
                ad.reverse()
                adAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun eventClick() {
        backToMain.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        addAd.setOnClickListener { startActivity(Intent(this, FormAD::class.java)) }
    }

    override fun onClickListner(adModel: AdModel) {
        val intent = Intent(this, FormAD::class.java);
        intent.putExtra("ad", adModel);
        startActivity(intent);
    }
}

