package com.example.waypointjournalosm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

class MenuComparisonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compare_menu_layout)

        // Fetch restaurant details
        val restaurant1 = intent.getStringExtra("restaurant1")
        val menu1 = intent.getStringExtra("menu1")
        val ingredients1 = intent.getStringArrayExtra("ingredients1")?.toList()
        val price1 = intent.getIntExtra("price1", 0)

        val restaurant2 = intent.getStringExtra("restaurant2")
        val menu2 = intent.getStringExtra("menu2")
        val ingredients2 = intent.getStringArrayExtra("ingredients2")?.toList()
        val price2 = intent.getIntExtra("price2", 0)

        // Bind to views (example)
        findViewById<TextView>(R.id.topRestaurantName).text = restaurant1
        findViewById<TextView>(R.id.topRestaurantMenuName).text = menu1
        findViewById<TextView>(R.id.topRestaurantMenuIngredients).text = ingredients1?.joinToString(", ")
        findViewById<TextView>(R.id.topRestaurantMenuPrice).text = "Price: P$price1"

        findViewById<TextView>(R.id.bottomRestaurantName).text = restaurant2
        findViewById<TextView>(R.id.bottomRestaurantMenuName).text = menu2
        findViewById<TextView>(R.id.bottomRestaurantMenuIngredients).text = ingredients2?.joinToString(", ")
        findViewById<TextView>(R.id.bottomRestaurantMenuPrice).text = "Price: P$price2"

        // Add listener for "Add to Itinerary" buttons
        findViewById<Button>(R.id.topOption).setOnClickListener {
            if (!restaurant1.isNullOrEmpty()) {
                showItineraryDialog(restaurant1)
            } else {
                Toast.makeText(this, "Restaurant 1 data is missing.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.bottomOption).setOnClickListener {
            if (!restaurant2.isNullOrEmpty()) {
                showItineraryDialog(restaurant2)
            } else {
                Toast.makeText(this, "Restaurant 2 data is missing.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Show the itinerary dialog
    private fun showItineraryDialog(restaurantName: String) {
        val dialogFragment = ItineraryDialogFragment.newInstance(restaurantName)
        dialogFragment.show(supportFragmentManager, "ItineraryDialog")
    }
}