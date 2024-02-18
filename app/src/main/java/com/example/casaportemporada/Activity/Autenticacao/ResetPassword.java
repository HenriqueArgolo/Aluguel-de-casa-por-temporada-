package com.example.casaportemporada.Activity.Autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casaportemporada.Helper.FirebaseHelper;
import com.example.casaportemporada.R;

public class ResetPassword extends AppCompatActivity {
    private ImageButton btn_back_to_login;
    private TextView text_message_resetPassword;

    private EditText text_resetpassword_email;

    private Button btn_resetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        startElements();
        eventClick();
    }


    private void startElements(){
        btn_back_to_login = findViewById(R.id.btn_resetPassword_back_login);
        text_message_resetPassword = findViewById(R.id.text_message_resetPassword);
        text_resetpassword_email = findViewById(R.id.text_resetpassword_email);
        btn_resetPassword = findViewById(R.id.btn_resetPaasword);
    }

    private void eventClick(){
        btn_back_to_login.setOnClickListener(view ->{
            startActivity(new Intent(this, LoginActivity.class));
        });
        btn_resetPassword.setOnClickListener(view ->{
            validateEmail();
        });

    }

    private void validateEmail(){
        String email = text_resetpassword_email.getText().toString();
        if (!email.isEmpty()){
            resetPassword(email);
        }else {
            text_resetpassword_email.requestFocus();
            text_resetpassword_email.setError("Digite seu email");
        }
    }

    public void resetPassword(String email){
        FirebaseHelper.getAuth().sendPasswordResetEmail(
            email
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
            text_message_resetPassword.setText("Acabamos de te enviar um email com um link para recupear sua senha.");
            }else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}


