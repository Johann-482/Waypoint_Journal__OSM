package com.example.waypointjournalosm

import android.content.Intent
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val map_button = findViewById<Button>(R.id.map_button)
        val journal_button = findViewById<Button>(R.id.journal_button)
        val itinerary_button = findViewById<Button>(R.id.itinerary_button)
        val compare_button = findViewById<Button>(R.id.compare_button)
        val logout_button = findViewById<Button>(R.id.logout_button)

        map_button.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        journal_button.setOnClickListener{
            val intent = Intent(this, JournalActivity::class.java)
            startActivity(intent)
        }
        itinerary_button.setOnClickListener{
            val intent = Intent(this, ItineraryActivity::class.java)
            startActivity(intent)
        }
        compare_button.setOnClickListener{
            val intent = Intent(this, CompareMenuItemActivity::class.java)
            startActivity(intent)
        }
        logout_button.setOnClickListener{
            logoutUser()
        }
        }
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut() // Sign out from Firebase
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
        startActivity(intent) // Navigate to LoginActivity
        finish() // Finish the current activity
    }
    }
