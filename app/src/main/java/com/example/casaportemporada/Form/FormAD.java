package com.example.casaportemporada.Form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.casaportemporada.Activity.MainActivity;
import com.example.casaportemporada.Model.AdModel;
import com.example.casaportemporada.R;

public class FormAD extends AppCompatActivity {
    private EditText ad_title;
    private EditText ad_description;
    private EditText ad_room_qtd;
    private EditText ad_bathroom_qtd;
    private EditText ad_garage_qtd;
    private ImageButton btn_back_property;
    private ImageButton btn_save_property;
    private CheckBox ad_checkbox;

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
    }

    private void eventClick(){
        btn_back_property.setOnClickListener(view ->{
            startActivity(new Intent(this, MainActivity.class));
        });
        btn_save_property.setOnClickListener(view ->{
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

}