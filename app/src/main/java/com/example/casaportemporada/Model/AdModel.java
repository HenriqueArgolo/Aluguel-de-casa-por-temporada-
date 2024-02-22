package com.example.casaportemporada.Model;

import com.example.casaportemporada.Helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class AdModel implements Serializable {
    private String id;

    private String userId;
    private String title;
    private String description;
    private String room;
    private String bathroom;
    private String garage;
    private Boolean state;
    private String imageUrl;



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AdModel(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }
    public void save(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("ad")
                .child(FirebaseHelper.getUserId())
                .child(this.getId());
        reference.setValue(this);

        DatabaseReference reference1 = FirebaseHelper.getDatabaseReference()
                .child("public_ad")
                .child(this.getId());
        reference1.setValue(this);

    }

    public void delete(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("ad")
                .child(FirebaseHelper.getUserId())
                .child(this.getId());
        reference.removeValue().addOnCompleteListener(taks ->{
            if(taks.isSuccessful()){
                StorageReference storageReference = FirebaseHelper.getStorangeReference()
                        .child("images")
                        .child("ad")
                        .child(FirebaseHelper.getUserId())
                        .child(this.id.toString() + ".jpg");
                storageReference.delete();
            }
        });
    }
    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }
}
