package com.example.waypointjournalosm

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private val restaurantItem: RestaurantItem by viewModels()
    private lateinit var restaurant1: String // The restaurant name to pass to the dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_place)

        // Set up the adapter with onAddClick behavior
        val listAdapter = ListAdapter(
            onAddClick = { restaurant ->
                // Handle the add click here: set the restaurant name for the dialog
                restaurant1 = restaurant.name
                // Trigger the dialog
                if (!restaurant1.isNullOrEmpty()) {
                    showItineraryDialog(restaurant1)
                } else {
                    Toast.makeText(this, "Restaurant data is missing.", Toast.LENGTH_SHORT).show()
                }
            },
            onInfoClick = { restaurant ->
                // Handle the info click if necessary
            }
        )


        val recyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter

        restaurantItem.restaurantList.observe(this, Observer { restaurantList ->
            listAdapter.submitList(restaurantList)
        })

        // Add text watcher to handle search
        val searchEditText = findViewById<EditText>(R.id.editTextText2)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Optionally handle actions before text change
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val query = charSequence.toString()
                // Filter the restaurant list based on the query
                listAdapter.filterList(query)
            }

            override fun afterTextChanged(editable: Editable?) {
                // Optionally handle actions after the text has changed
            }
        })
    }
    private fun showInfoDialog(item: RestaurantData) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Information about ${item.name}")
            .setCancelable(true)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Info")
        alert.show()
    }
    // Show the itinerary dialog with the selected restaurant
    private fun showItineraryDialog(restaurantName: String) {
        val dialogFragment = ItineraryDialogFragment.newInstance(restaurantName)
        // Ensure you're using the supportFragmentManager from the Activity
        dialogFragment.show(supportFragmentManager, "ItineraryDialog")
    }
}