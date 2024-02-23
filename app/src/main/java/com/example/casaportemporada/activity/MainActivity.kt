package com.example.casaportemporada.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.Model.AdModel
import com.example.casaportemporada.R
import com.example.casaportemporada.activity.autenticacao.LoginActivity
import com.example.casaportemporada.adapter.AdAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.Objects

class MainActivity : AppCompatActivity(), AdAdapter.OnClick  {
    private var ad: MutableList<AdModel> = ArrayList()
    private var ib_menu: ImageButton? = null
    private var rv_main: RecyclerView? = null
    private var text_loading: TextView? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var adAdapter: AdAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startComponnents()
        configClicks()
        configRv()

    }

    override fun onStart() {
        super.onStart()
        recoverAD()
    }

    private fun configRv() {
        rv_main?.layoutManager = LinearLayoutManager(this)
        rv_main?.setHasFixedSize(true);
        adAdapter = AdAdapter(ad, this)
        rv_main?.adapter = adAdapter

    }
    private fun recoverAD() {
        val reference : DatabaseReference = FirebaseHelper.getDatabaseReference()
            .child("public_ad")
    reference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                ad.clear()
                for(snap in snapshot.children){
                    val newAd = snap.getValue(AdModel::class.java)
                    newAd?.let{ad.add(it)}
                }
            }else{
                progressBar.visibility = View.INVISIBLE
                text_loading?.setText("Ainda não há anuncios cadastrados")
            }

            progressBar.visibility =View.INVISIBLE
            text_loading?.setText("")
            ad.reverse()
            adAdapter.notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {

        }

    })


    }
    private fun configClicks() {
        ib_menu!!.setOnClickListener { v: View? ->
            val popupMenu = PopupMenu(this, ib_menu)
            popupMenu.menuInflater.inflate(R.menu.main_menu_option, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                if (menuItem.itemId == R.id.filter) {
                    startActivity(Intent(this, FilterActivity::class.java))
                } else if (menuItem.itemId == R.id.myAd) {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(Intent(this, MyAdActivity::class.java))
                    } else {
                        showDialogLogin()
                    }
                } else {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(Intent(this, MyAccountActivity::class.java))
                    } else {
                        showDialogLogin()
                    }
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun startComponnents() {
        ib_menu = findViewById(R.id.ib_menu)
        rv_main = findViewById(R.id.recyclerViewMain)
        text_loading = findViewById(R.id.loading_text)
        progressBar = findViewById(R.id.loading_ad_progressbar)
    }

    private fun showDialogLogin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Fazer login")
        builder.setTitle("Você precisa fazer login para acessar essa função, deseja fazer login?")
        builder.setCancelable(true)
        builder.setNegativeButton("Não") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        builder.setPositiveButton("Sim") { dialog: DialogInterface?, which: Int ->
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onClickListner(adModel: AdModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("public_ad", adModel)
        startActivity(intent)
    }
}