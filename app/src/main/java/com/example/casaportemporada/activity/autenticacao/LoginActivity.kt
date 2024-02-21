package com.example.casaportemporada.activity.autenticacao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.R
import com.example.casaportemporada.form.FormAD

class LoginActivity : AppCompatActivity() {
    private lateinit var registerButton: TextView
    private lateinit var resetPassword: TextView
    private lateinit var textEmail: EditText
    private lateinit var textPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        starElements()
        eventClick()
    }

    private fun starElements() {
        registerButton = findViewById(R.id.btn_register)
        resetPassword = findViewById(R.id.btn_resetPaasword)
        textEmail = findViewById(R.id.text_email)
        textPassword = findViewById(R.id.text_password)
        progressBar = findViewById(R.id.login_progressbar)
        btnLogin = findViewById(R.id.btn_login)
    }

    private fun eventClick() {
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        resetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPassword::class.java))
        }
        btnLogin.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = textEmail.text.toString()
        val password = textPassword.text.toString()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                logar(email, password)
                progressBar.visibility = View.VISIBLE
                startActivity(Intent(this, FormAD::class.java))
            } else {
                textPassword.requestFocus()
                textPassword.setError("Informe sua senha")
            }
        } else {
            textEmail.requestFocus()
            textEmail.setError("Informe seu email")
        }
    }

    private fun logar(email: String, password: String) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                finish()
            } else {
                val error = task.exception?.message
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
            }
        }
    }
}
