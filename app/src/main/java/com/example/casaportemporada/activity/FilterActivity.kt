package com.example.casaportemporada.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toolbar
import com.example.casaportemporada.Model.FilterModel
import com.example.casaportemporada.R
import com.google.firebase.database.core.view.View
import kotlin.properties.Delegates

class FilterActivity : AppCompatActivity() {

    private lateinit var room_text: TextView
    private lateinit var room_filter: SeekBar
    private lateinit var bathroom_text: TextView
    private lateinit var bathroom_filter: SeekBar
    private lateinit var garage_text: TextView
    private lateinit var garage_filter: SeekBar
    private lateinit var clear_filter: Button
    private lateinit var filter: Button
    private lateinit var toolbar_text: TextView
    private var filterModel: FilterModel = FilterModel()

    private var roomQtd: Int =0
    private var bathroomQtd: Int = 0
    private var garageQtd: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        starElements()
        eventClick()
        configSB()
    }

    private fun eventClick() {
        clear_filter.setOnClickListener {
            clearFilter()
        }
    }

    private fun filter(){
        if (roomQtd >0 ) filterModel.roomFilter = roomQtd
        if(bathroomQtd > 0) filterModel.bathroomFilter = bathroomQtd
        if (garageQtd > 0) filterModel.garageFilter = garageQtd

        val intent : Intent = Intent(this@FilterActivity, MainActivity::class.java)

        if (roomQtd > 0 || bathroomQtd > 0 || garageQtd > 0){
            intent.putExtra("filter", FilterModel::class.java)
            startActivity(intent)
        }
    }

    private fun configSB() {
        room_filter.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress > 0) {
                    room_text.text = "$progress quartos ou mais"
                    roomQtd = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        bathroom_filter.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress > 0) {
                    bathroom_text.text = "$progress banheiros ou mais"
                    bathroomQtd = progress
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        garage_filter.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress > 0) {
                    garage_text.text = "$progress garagens ou mais"
                    garageQtd = progress
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun starElements() {
        toolbar_text = findViewById(R.id.text_salvar)
        toolbar_text.setText("Filtrar anuncios")
        room_text = findViewById(R.id.room_filter_text)
        room_filter = findViewById(R.id.room_filter)
        bathroom_text = findViewById(R.id.bathroom_filter_text)
        bathroom_filter = findViewById(R.id.bathroom_filter)
        garage_text = findViewById(R.id.garage_filter_ext)
        garage_filter = findViewById(R.id.garage_filter)
        clear_filter = findViewById(R.id.clear_filter)
        filter = findViewById(R.id.filter)
    }


    private fun clearFilter() {
        roomQtd = 0
        bathroomQtd = 0
        garageQtd = 0

        room_filter.setProgress(0)
        bathroom_filter.setProgress(0)
        garage_filter.setProgress(0)
        room_text.text = "0 quartos ou mais"
        bathroom_text.text = "0 baheiros ou mais"
        garage_text.text = "0 garagens ou mais"
        finish();

    }
}