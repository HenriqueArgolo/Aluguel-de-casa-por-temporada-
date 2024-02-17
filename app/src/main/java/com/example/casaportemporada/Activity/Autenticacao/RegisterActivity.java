package com.example.casaportemporada.Activity.Autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.casaportemporada.Activity.MainActivity;
import com.example.casaportemporada.Helper.FirebaseHelper;
import com.example.casaportemporada.Model.User;
import com.example.casaportemporada.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ImageButton btn_resgister_back_to_login;
    private EditText text_register_fullName;

    private EditText text_register_email;

    private EditText text_register_phone;

    private EditText text_register_password;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        startElements();
        eventClick();
    }

    private void startElements() {
        progressBar = findViewById(R.id.progress_register);
        btn_resgister_back_to_login = findViewById(R.id.btn_register_back_login);
        text_register_fullName = findViewById(R.id.text_register_fullName);
        text_register_email = findViewById(R.id.text_register_email);
        text_register_phone = findViewById(R.id.text_register_phone);
        text_register_password = findViewById(R.id.text_register_password);
    }

    private void eventClick() {
        btn_resgister_back_to_login.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    public void validateData(View view) {
        String name = text_register_fullName.getText().toString();
        String email = text_register_email.getText().toString();
        String phone = text_register_phone.getText().toString();
        String password = text_register_password.getText().toString();

        if (!name.isEmpty()) {
            if (!email.isEmpty()) {
                if (!phone.isEmpty()) {
                    if (!password.isEmpty()) {

                        User user = new User();
                        user.setName(name);
                        user.setEmail(email);
                        user.setPhone(phone);
                        user.setPassword(password);

                        resgisterUser(user);
                        progressBar.setVisibility(View.VISIBLE);
                        startActivity(new Intent(this, MainActivity.class));

                    } else {
                        text_register_password.requestFocus();
                        text_register_password.setError("Digite uma senha");
                    }

                } else {
                    text_register_phone.requestFocus();
                    text_register_phone.setError("Digite um telefone");
                }


            } else {
                text_register_email.requestFocus();
                text_register_email.setError("Digite um email");
            }

        } else {
            text_register_fullName.requestFocus();
            text_register_fullName.setError("Preencha seu nome");

        }

    }
    private void resgisterUser(User user){
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
              user.getEmail(), user.getPassword()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String id = Objects.requireNonNull(task.getResult()).getUser().getUid();
                user.setId(id);
                user.SaveUser();
                finish();
            }else {
                String result = task.getException().getMessage();
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
        });
    }
}