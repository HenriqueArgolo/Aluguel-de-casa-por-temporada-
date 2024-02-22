package com.example.casaportemporada.form

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.casaportemporada.Helper.FirebaseHelper
import com.example.casaportemporada.Model.AdModel
import com.example.casaportemporada.R
import com.example.casaportemporada.activity.MyAdActivity
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.squareup.picasso.Picasso
import java.io.IOException
import java.io.Serializable

@Suppress("DEPRECATION")
class FormAD : AppCompatActivity() {
    private val REQUEST_GALERIA = 100
    private lateinit var ad_title: EditText
    private lateinit var ad_description: EditText
    private lateinit var ad_room_qtd: EditText
    private lateinit var ad_bathroom_qtd: EditText
    private lateinit var ad_garage_qtd: EditText
    private lateinit var btn_back_property: ImageButton
    private lateinit var btn_save_property: ImageButton
    private lateinit var ad_checkbox: CheckBox
    private lateinit var img_ad: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var text_salvar: TextView
    private var imagePath: String? = null
    private var image: Bitmap? = null
    private  var ad: AdModel = AdModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_ad)

        startElements()
        eventClick()

        val extras = intent.extras;
        if (extras != null){
           ad = extras.getSerializable("ad") as AdModel;
            configData(ad);
        }

    }

    private fun startElements() {
        ad_title = findViewById(R.id.ad_title)
        ad_description = findViewById(R.id.ad_description)
        ad_room_qtd = findViewById(R.id.ad_room_qtd)
        ad_bathroom_qtd = findViewById(R.id.ad_bathroom_qtd)
        ad_garage_qtd = findViewById(R.id.ad_garage_qtd)
        btn_back_property = findViewById(R.id.btn_back_property)
        btn_save_property = findViewById(R.id.btn_save_property)
        ad_checkbox = findViewById(R.id.ad_checkbox)
        img_ad = findViewById(R.id.img_ad)
        progressBar = findViewById(R.id.progressbar_save)
        text_salvar = findViewById(R.id.text_salvar);
    }

    private fun configData(ad : AdModel){
        text_salvar.setText("Editar anuncio")
        Picasso.get().load(ad.imageUrl).into(img_ad);
        ad_title.setText(ad.title);
        ad_description.setText(ad.description);
        ad_room_qtd.setText(ad.room);
        ad_bathroom_qtd.setText(ad.bathroom);
        ad_garage_qtd.setText(ad.garage);
        ad_checkbox.isChecked = ad.state == ad.state;

    }

    private fun eventClick() {
        btn_back_property.setOnClickListener {
            startActivity(Intent(this, MyAdActivity::class.java))
        }
        btn_save_property.setOnClickListener {
            validateData()
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun validateData() {
        val title = ad_title.text.toString()
        val description = ad_description.text.toString()
        val room = ad_room_qtd.text.toString()
        val bathroom = ad_bathroom_qtd.text.toString()
        val garage = ad_garage_qtd.text.toString()

        if (title.isNotEmpty()) {
            if (description.isNotEmpty()) {
                if (room.isNotEmpty()) {
                    if (bathroom.isNotEmpty()) {
                        if (garage.isNotEmpty()) {
                                ad.userId = FirebaseHelper.getUserId()
                                ad.title = title
                                ad.description = description
                                ad.room = room
                                ad.bathroom = bathroom
                                ad.garage = garage
                                ad.state = ad_checkbox.isChecked

                            if (imagePath != null) {
                                saveAd()
                            } else {
                               if(ad.imageUrl != null){
                                   ad.save()
                                   finish();
                               }else{
                                   Toast.makeText(this, "Erro ao editar anuncio", Toast.LENGTH_LONG).show();
                               }
                            }
                        } else {
                            ad_garage_qtd.requestFocus()
                            ad_garage_qtd.error = "informe"
                        }
                    } else {
                        ad_bathroom_qtd.requestFocus()
                        ad_bathroom_qtd.error = "informe"
                    }
                } else {
                    ad_room_qtd.requestFocus()
                    ad_room_qtd.error = "informe"
                }
            } else {
                ad_description.requestFocus()
                ad_description.error = "Dê uma descrição para o seu anúncio"
            }
        } else {
            ad_title.requestFocus()
            ad_title.error = "Dê um título ao seu anúncio"
        }
    }

    fun verifyGalleryPermission(view: View) {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openGallery()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(this@FormAD, "Permissão negada.", Toast.LENGTH_LONG).show()
            }
        }
        showDilogGalleryPermission(permissionListener)
    }

    private fun showDilogGalleryPermission(listener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(listener)
            .setDeniedMessage("Se você negar esse serviço, você não pode carregar uma foto.")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALERIA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALERIA && resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            imagePath = selectedImageUri.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(baseContext.contentResolver, selectedImageUri!!)
                try {
                    image = ImageDecoder.decodeBitmap(source)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
            img_ad.setImageBitmap(image)
        }
    }

    private fun saveAd() {
        val storageReference: StorageReference = FirebaseHelper.getStorangeReference()
            .child("images")
            .child("ad")
            .child(FirebaseHelper.getUserId())
            .child(ad.id.toString() + ".jpg")

        val uploadTask: UploadTask = storageReference.putFile(Uri.parse(imagePath))
        uploadTask.addOnSuccessListener { _ ->
            storageReference.downloadUrl.addOnSuccessListener { task ->
                val imageUrl: Uri = task
                ad.imageUrl = imageUrl.toString()
                ad.save()
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
