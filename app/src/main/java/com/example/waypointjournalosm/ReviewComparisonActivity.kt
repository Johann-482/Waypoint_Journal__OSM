package com.example.waypointjournalosm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class ReviewComparisonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compare_reviews_layout)

        // Get the data passed from the intent
        val restaurant1Name = intent.getStringExtra("restaurant1")
        val review1 = intent.getStringExtra("review1")
        val review2 = intent.getStringExtra("review2")

        val restaurant2Name = intent.getStringExtra("restaurant2")
        val review3 = intent.getStringExtra("review3")
        val review4 = intent.getStringExtra("review4")

        // Display the restaurant names
        findViewById<TextView>(R.id.topReviewRestaurantName).text = restaurant1Name
        findViewById<TextView>(R.id.bottomReviewRestaurantName).text = restaurant2Name

        // Set up ViewPager2 for top reviews
        val topReviewsViewPager = findViewById<ViewPager2>(R.id.topReviewsViewPager)
        val topReviews = listOf(review1, review2) // List of reviews
        val topAdapter = ReviewPagerAdapter(topReviews as List<String>)
        topReviewsViewPager.adapter = topAdapter

        // Set up ViewPager2 for bottom reviews
        val bottomReviewsViewPager = findViewById<ViewPager2>(R.id.bottomReviewsViewPager)
        val bottomReviews = listOf(review3, review4) // List of reviews
        val bottomAdapter = ReviewPagerAdapter(bottomReviews as List<String>)
        bottomReviewsViewPager.adapter = bottomAdapter

        // Add listener for "Add to Itinerary" buttons
        findViewById<Button>(R.id.topReviewOption).setOnClickListener {
            if (!restaurant1Name.isNullOrEmpty()) {
                showItineraryDialog(restaurant1Name)
            } else {
                Toast.makeText(this, "Restaurant 1 data is missing.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.bottomReviewOption).setOnClickListener {
            if (!restaurant2Name.isNullOrEmpty()) {
                showItineraryDialog(restaurant2Name)
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
