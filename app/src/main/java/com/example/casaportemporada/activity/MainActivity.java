package com.example.casaportemporada.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.casaportemporada.Helper.FirebaseHelper;
import com.example.casaportemporada.R;
import com.example.casaportemporada.activity.autenticacao.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private ImageButton ib_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startComponnents();
        configClicks();
    }


    private void configClicks() {
        ib_menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, ib_menu);
            popupMenu.getMenuInflater().inflate(R.menu.main_menu_option, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.filter) {
                    startActivity(new Intent(this, FilterActivity.class));
                } else if (menuItem.getItemId() == R.id.myAd) {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MyAdActivity.class));
                    } else {
                        showDialogLogin();
                    }
                    ;
                } else {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MyAccountActivity.class));
                    } else {
                        showDialogLogin();
                    }
                }
                return true;
            });
            popupMenu.show();
            ;
        });
    }

    private void startComponnents() {
        ib_menu = findViewById(R.id.ib_menu);

    }

    private void showDialogLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fazer login");
        builder.setTitle("Você precisa fazer login para acessar essa função, deseja fazer login?");
        builder.setCancelable(true);
        builder.setNegativeButton("Não", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Sim", (dialog, which) -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}