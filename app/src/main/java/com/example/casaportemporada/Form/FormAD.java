package com.example.casaportemporada.Form;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
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
import com.example.casaportemporada.Activity.MyAdActivity;
import com.example.casaportemporada.Helper.FirebaseHelper;
import com.example.casaportemporada.Model.AdModel;
import com.example.casaportemporada.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

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
    private AdModel ad ;

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
            startActivity(new Intent(this, MyAdActivity.class));
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

                            if(ad == null) ad = new AdModel();
                            ad.setTitle(title);
                            ad.setDescription(description);
                            ad.setRoom(room);
                            ad.setBathroom(bathroom);
                            ad.setGarage(garage);
                            ad.setState(ad_checkbox.isChecked());

                            if (imagePath != null){
                                saveAd();
                            }else {
                                Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_LONG).show();
                            }
                        }else {
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
                Uri selectedImageUri = data.getData();
                imagePath = Objects.requireNonNull(selectedImageUri).toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), selectedImageUri);
                try {
                    image = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            img_ad.setImageBitmap(image);
        }
        }

    private void saveAd() {
        StorageReference storageReference = FirebaseHelper.getStorangeReference()
                .child("images")
                .child("ad")
                .child(FirebaseHelper.getUserId())
                .child(ad.getId() + ".jpg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(imagePath));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(task -> {
                String imageUrl = task.toString();
                ad.setImageUrl(imageUrl);
                ad.save();
                finish();
            }).addOnFailureListener(e ->{
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e ->{
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}