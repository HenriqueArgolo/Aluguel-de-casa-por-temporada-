package com.example.casaportemporada.Activity.Autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casaportemporada.Activity.MainActivity;
import com.example.casaportemporada.Helper.FirebaseHelper;
import com.example.casaportemporada.R;

public class LoginActivity extends AppCompatActivity {
    private TextView register_button;
    private TextView reset_passwrod;
    private EditText text_email;
    private  EditText text_password;
    private ProgressBar progressBar;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        starElements();
        eventClick();
    }


    private void starElements(){
        register_button = findViewById(R.id.btn_register);
        reset_passwrod = findViewById(R.id.btn_resetPaasword);
        text_email = findViewById(R.id.text_email);
        text_password = findViewById(R.id.text_password);
        progressBar = findViewById(R.id.login_progressbar);
        btn_login = findViewById(R.id.btn_login);
    }

    private void eventClick(){
        register_button.setOnClickListener(view ->{
            startActivity(new Intent(this, RegisterActivity.class));
        });
        reset_passwrod.setOnClickListener(view ->{
            startActivity(new Intent(this, ResetPassword.class));

        });
        btn_login.setOnClickListener(view ->{
            validateData();
        });
    }

    private void validateData(){
        String email = text_email.getText().toString();
        String passsword = text_password.getText().toString();

        if(!email.isEmpty()){
            if(!passsword.isEmpty()){
                logar(email, passsword);
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(this, MainActivity.class));
            }else {
                text_password.requestFocus();
                text_password.setError("Informe sua senha");
            }
        }else {
            text_email.requestFocus();
            text_email.setError("informe seu email");
        }
    }

    private void logar(String email, String password){
        FirebaseHelper.getAuth().signInWithEmailAndPassword(
                email, password
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                finish();
            }else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}