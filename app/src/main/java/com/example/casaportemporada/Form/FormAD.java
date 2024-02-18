package com.example.casaportemporada.Form;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.casaportemporada.Activity.MainActivity;
import com.example.casaportemporada.Model.AdModel;
import com.example.casaportemporada.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.net.URI;
import java.util.List;

public class FormAD extends AppCompatActivity {
    private static final int REQUEST_GALERIA = 100;
    private EditText ad_title;
    private EditText ad_description;
    private EditText ad_room_qtd;
    private EditText ad_bathroom_qtd;
    private EditText ad_garage_qtd;
    private ImageButton btn_back_property;
    private ImageButton btn_save_property;
    private CheckBox ad_checkbox;
    private ImageView img_ad;
    private String imagePath;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ad);
        starElements();
        eventClick();
    }

    private void starElements() {
        ad_title = findViewById(R.id.ad_title);
        ad_description = findViewById(R.id.ad_description);
        ad_room_qtd = findViewById(R.id.ad_room_qtd);
        ad_bathroom_qtd = findViewById(R.id.ad_bathroom_qtd);
        ad_garage_qtd = findViewById(R.id.ad_garage_qtd);
        btn_back_property = findViewById(R.id.btn_back_property);
        btn_save_property = findViewById(R.id.btn_save_property);
        ad_checkbox = findViewById(R.id.ad_checkbox);
        img_ad = findViewById(R.id.img_ad);

    }

    private void eventClick() {
        btn_back_property.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        btn_save_property.setOnClickListener(view -> {
            valitadeData();
        });

    }

    private void valitadeData() {
        String title = ad_title.getText().toString();
        String description = ad_description.getText().toString();
        String room = ad_room_qtd.getText().toString();
        String bathroom = ad_bathroom_qtd.getText().toString();
        String garage = ad_garage_qtd.getText().toString();

        if (!title.isEmpty()) {
            if (!description.isEmpty()) {
                if (!room.isEmpty()) {
                    if (!bathroom.isEmpty()) {
                        if (!garage.isEmpty()) {

                            AdModel ad = new AdModel();
                            ad.setTitle(title);
                            ad.setDescription(description);
                            ad.setRoom(room);
                            ad.setBathroom(bathroom);
                            ad.setGarage(garage);
                            ad.setState(ad_checkbox.isChecked());
                        }
                        {
                            ad_garage_qtd.requestFocus();
                            ad_garage_qtd.setError("informe");
                        }
                    } else {
                        ad_bathroom_qtd.requestFocus();
                        ad_bathroom_qtd.setError("informe");
                    }
                } else {
                    ad_room_qtd.requestFocus();
                    ad_room_qtd.setError("informe");
                }
            } else {
                ad_description.requestFocus();
                ad_description.setError("Dê uam descrição para o seu anuncio");
            }

        } else {
            ad_title.requestFocus();
            ad_title.setError("Dê um titul oao seu anuncio");
        }
    }

    public void verifyGalleryPermission(View view) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAD.this, "Permissão negada.", Toast.LENGTH_LONG).show();

            }
        };
        showDilogGalleryPermission(permissionListener);
    }


    private void showDilogGalleryPermission(PermissionListener listener) {
        TedPermission.create()
                .setPermissionListener(listener)
                .setDeniedMessage("Se você negar esse serviço, você não pode carregar uma foto.")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALERIA && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    img_ad.setImageURI(selectedImageUri);
                }
            }
        }
    }



}