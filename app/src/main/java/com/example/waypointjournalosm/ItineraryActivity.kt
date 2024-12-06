package com.example.waypointjournalosm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase



class ItineraryActivity : AppCompatActivity() {

    private lateinit var itineraryList: MutableList<ItineraryItem> // This will hold the fetched data
    private lateinit var itineraryAdapter: ChecklistAdapter // Your RecyclerView Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itinerary_activity)

        // Initialize the list and adapter
        itineraryList = mutableListOf()
        itineraryAdapter = ChecklistAdapter(this, itineraryList) // Set up RecyclerView Adapter

        // Set up the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.checklistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itineraryAdapter

        // Fetch data from Firebase
        fetchItineraryFromFirebase()
    }

    private fun fetchItineraryFromFirebase() {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("Itinerary") // Firebase root or specific path
        firebaseDatabase.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                itineraryList.clear() // Clear any existing data
                snapshot.children.forEach { child ->
                    val restaurantName = child.child("restaurantName").getValue(String::class.java)
                    val mealType = child.child("mealType").getValue(String::class.java)

                    // Only add If both fields are not null
                    if (restaurantName != null && mealType != null) {
                        val itineraryItem = ItineraryItem(restaurantName, mealType)
                        itineraryList.add(itineraryItem) // Add the item to the list
                        Log.d("FirebaseData", "Fetched Item: Restaurant: $restaurantName, Meal Type: $mealType")
                    }
                }
                itineraryAdapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }
        }.addOnFailureListener {
            // Handle any errors
            Log.e("FirebaseError", "Error fetching data: ${it.message}")
        }
    }
}