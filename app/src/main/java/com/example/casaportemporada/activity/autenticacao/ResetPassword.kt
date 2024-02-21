package com.example.casaportemporada.activity.autenticacao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.R
import com.google.android.gms.tasks.Task

class ResetPassword : AppCompatActivity() {
    private var btn_back_to_login: ImageButton? = null
    private var text_message_resetPassword: TextView? = null
    private var text_resetpassword_email: EditText? = null
    private var btn_resetPassword: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        startElements()
        eventClick()
    }

    private fun startElements() {
        btn_back_to_login = findViewById(R.id.btn_resetPassword_back_login)
        text_message_resetPassword = findViewById(R.id.text_message_resetPassword)
        text_resetpassword_email = findViewById(R.id.text_resetpassword_email)
        btn_resetPassword = findViewById(R.id.btn_resetPaasword)
    }

    private fun eventClick() {
        btn_back_to_login!!.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }
        btn_resetPassword!!.setOnClickListener { view: View? -> validateEmail() }
    }

    private fun validateEmail() {
        val email = text_resetpassword_email!!.text.toString()
        if (!email.isEmpty()) {
            resetPassword(email)
        } else {
            text_resetpassword_email!!.requestFocus()
            text_resetpassword_email!!.error = "Digite seu email"
        }
    }

    fun resetPassword(email: String?) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(
            email!!
        ).addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                text_message_resetPassword!!.text =
                    "Acabamos de te enviar um email com um link para recupear sua senha."
            } else {
                val error = task.exception!!.message
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
