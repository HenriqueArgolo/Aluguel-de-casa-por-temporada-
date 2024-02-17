package com.example.casaportemporada.Activity.Autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.casaportemporada.R;

public class ResetPassword extends AppCompatActivity {
    private ImageButton btn_back_to_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        startElements();
        eventClick();
    }


    private void startElements(){
        btn_back_to_login = findViewById(R.id.btn_resetPassword_back_login);
    }

    private void eventClick(){
        btn_back_to_login.setOnClickListener(view ->{
            startActivity(new Intent(this, LoginActivity.class));
        });

    }

}


