package com.example.casaportemporada.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.Model.User
import com.example.casaportemporada.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class UserEditActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private  lateinit var user : User
    private lateinit var salvar : ImageButton
    private lateinit var progressbar: ProgressBar
    private lateinit var backtomain : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)
        starElements()
        recoveryData()
    }

    private fun starElements() {
        backtomain = findViewById(R.id.btn_back_property)
        progressbar = findViewById(R.id.progress_register)
        name = findViewById(R.id.edit_nome)
        email = findViewById(R.id.edit_email)
        phone = findViewById(R.id.edit_phone)
        salvar = findViewById(R.id.btn_save_property)
        salvar.setOnClickListener{
            progressbar.visibility = View.VISIBLE
            validateData()
            finish()
        }
        backtomain.setOnClickListener{
            startActivity(Intent(this, MyAdActivity::class.java))
        }
    }

    fun validateData() {
        val name1 = name.text.toString()
        val phone1 = phone.text.toString()
        val emaul1 = email.text.toString()

        if (name1.isNotEmpty()) {
            if (phone1.isNotEmpty()) {
                user.name = name1
                user.phone =phone1
                user.email = emaul1
                user.SaveUser()

            } else {
                phone.requestFocus()
                phone.error = "Digite um telefone"
            }
        } else {
            name.requestFocus()
            name.error = "Digite um nome"
        }
    }

    private fun recoveryData() {
    val reference : DatabaseReference = FirebaseHelper.getDatabaseReference()
        .child("users")
        .child(FirebaseHelper.getUserId())

        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userdata = snapshot.getValue(User::class.java)
                this@UserEditActivity.user = userdata!!
                configData()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun configData(){
        name.setText(user.name)
        phone.setText(user.phone)
        email.setText(user.email)
    }
}
