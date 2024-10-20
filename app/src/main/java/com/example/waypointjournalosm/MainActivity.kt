package com.example.waypointjournalosm

import android.content.Intent
import android.widget.Button

class MainActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val map_button = findViewById<Button>(R.id.map_button)
        //val journal_button = findViewById<Button>(R.id.journal_button)

        map_button.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        /*
        journal_button.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
         */
        }
    }
