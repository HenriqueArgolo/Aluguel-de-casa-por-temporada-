package com.example.casaportemporada.activity.autenticacao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.Model.User
import com.example.casaportemporada.R
import com.example.casaportemporada.activity.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import java.util.Objects

class RegisterActivity : AppCompatActivity() {
    private var btn_resgister_back_to_login: ImageButton? = null
    private var text_register_fullName: EditText? = null
    private var text_register_email: EditText? = null
    private var text_register_phone: EditText? = null
    private var text_register_password: EditText? = null
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        startElements()
        eventClick()
    }

    private fun startElements() {
        progressBar = findViewById(R.id.progress_register)
        btn_resgister_back_to_login = findViewById(R.id.btn_register_back_login)
        text_register_fullName = findViewById(R.id.text_register_fullName)
        text_register_email = findViewById(R.id.text_register_email)
        text_register_phone = findViewById(R.id.text_register_phone)
        text_register_password = findViewById(R.id.text_register_password)
    }

    private fun eventClick() {
        btn_resgister_back_to_login!!.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }
    }

    fun validateData(view: View?) {
        val name = text_register_fullName!!.text.toString()
        val email = text_register_email!!.text.toString()
        val phone = text_register_phone!!.text.toString()
        val password = text_register_password!!.text.toString()
        if (!name.isEmpty()) {
            if (!email.isEmpty()) {
                if (!phone.isEmpty()) {
                    if (!password.isEmpty()) {
                        val user = User()
                        user.name = name
                        user.email = email
                        user.phone = phone
                        user.password = password
                        resgisterUser(user)
                        progressBar!!.visibility = View.VISIBLE
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        text_register_password!!.requestFocus()
                        text_register_password!!.error = "Digite uma senha"
                    }
                } else {
                    text_register_phone!!.requestFocus()
                    text_register_phone!!.error = "Digite um telefone"
                }
            } else {
                text_register_email!!.requestFocus()
                text_register_email!!.error = "Digite um email"
            }
        } else {
            text_register_fullName!!.requestFocus()
            text_register_fullName!!.error = "Preencha seu nome"
        }
    }

    private fun resgisterUser(user: User) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
            user.email, user.password
        ).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val id = Objects.requireNonNull(task.result).user!!
                    .uid
                user.id = id
                user.SaveUser()
                finish()
            } else {
                val result = task.exception!!.message
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        }
    }
}