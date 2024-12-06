package com.example.waypointjournalosm

import com.example.waypointjournalosm.utils.stringSimilarity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider

class CompareMenuItemActivity : AppCompatActivity() {
    private val restaurantItem: RestaurantItem by viewModels()
    private lateinit var compareListAdapter: CompareListAdapter

    // Declare views for the layouts
    private lateinit var dropdownMenu: AutoCompleteTextView
    private lateinit var thirdLayout: LinearLayout
    private lateinit var compareButton: Button

    private lateinit var restaurantItemViewModel: RestaurantItem

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    private var isMenuSelected: Boolean = true // Default to Menu comparison

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compare_item)

        // Initialize ViewModel
        restaurantItemViewModel = ViewModelProvider(this).get(RestaurantItem::class.java)

        // Initialize the RecyclerView and adapter
        compareListAdapter = CompareListAdapter(
            onAddClick = { item ->
                Toast.makeText(this, "Add ${item.name} to List", Toast.LENGTH_SHORT).show()
            },
            onInfoClick = { item ->
                // Handle info click (optional)
            },
            onItemClick = { selectedRestaurant ->
                // Update bottom layout when a restaurant is selected
            }
        )

        val recyclerView = findViewById<RecyclerView>(R.id.compareRestaurantRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = compareListAdapter

        // Observe restaurant list from ViewModel
        restaurantItem.restaurantList.observe(this, Observer { restaurantList ->
            compareListAdapter.submitList(restaurantList)
        })



        // Initialize dropdown and third layout
        dropdownMenu = findViewById(R.id.autoing_complete_text)
        thirdLayout = findViewById(R.id.thirdLinearLayout)

        // Set up the dropdown menu items
        val options = listOf("Menu", "Review")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, options)
        dropdownMenu.setAdapter(adapter)

        // Add listener for dropdown menu selection
        dropdownMenu.setOnItemClickListener { _, _, position, _ ->
            when (options[position]) {
                "Menu" -> handleMenuSelection()
                "Review" -> handleReviewSelection()
            }
        }

        // Observe restaurant list changes
        restaurantItemViewModel.restaurantList.observe(this, Observer { restaurantList ->
            compareListAdapter.submitList(restaurantList)
        })
        compareButton = findViewById(R.id.button3)

        // Set up compare button click listener
        compareButton.setOnClickListener {
            handleCompareButtonClick()
        }

        // Get the EditText from the third layout
        val menuFilterEditText: EditText = findViewById(R.id.editTextText)

        // Set listener for text changes in the EditText
        menuFilterEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Optionally handle actions before the text is changed (e.g., clear filter when backspacing)
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val query = charSequence.toString()

                // Remove any previously scheduled filter task
                runnable?.let { handler.removeCallbacks(it) }

                // Schedule a new filtering task with a small delay (debounce effect)
                runnable = Runnable {
                    compareListAdapter.filterList(query)  // Filter after the debounce delay
                }

                // Post the task with a debounce delay (e.g., 300ms)
                handler.postDelayed(runnable!!, 300)
            }


            override fun afterTextChanged(editable: Editable?) {
                // Optional: Handle actions after the text has changed
            }
        })

    }

    // Function to handle the "Menu" option
    private fun handleMenuSelection() {
        // Make third layout visible
        thirdLayout.visibility = View.VISIBLE
        isMenuSelected = true

        // Prepare for menu comparison
        Toast.makeText(this, "Prepare for Menu Comparison", Toast.LENGTH_SHORT).show()
    }

    // Function to handle the "Review" option
    private fun handleReviewSelection() {
        // Make third layout invisible
        thirdLayout.visibility = View.GONE
        isMenuSelected = false

        // Prepare for review comparison
        Toast.makeText(this, "Prepare for Review Comparison", Toast.LENGTH_SHORT).show()
    }
    private fun handleCompareButtonClick() {
        // Check if the user has selected an option from the dropdown menu
        if (!::dropdownMenu.isInitialized || dropdownMenu.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select an option (Menu or Review) first.", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the selected restaurants from the adapter
        val selectedRestaurants = compareListAdapter.getSelectedRestaurants()

        if (selectedRestaurants.size != 2) {
            Toast.makeText(this, "Please select exactly 2 restaurants for comparison.", Toast.LENGTH_SHORT).show()
            return
        }

        val restaurant1 = selectedRestaurants[0]
        val restaurant2 = selectedRestaurants[1]


        // If "Menu" is selected, start MenuComparisonActivity
        if (isMenuSelected) {
            // Check menu similarity
            if (!isMenuSimilar(restaurant1.rMenu, restaurant2.rMenu)) {
                Toast.makeText(this, "The menus are not similar enough for comparison.", Toast.LENGTH_SHORT).show()
                return
            }
            // Navigate to MenuComparisonActivity with the selected restaurants
            val intent = Intent(this, MenuComparisonActivity::class.java).apply {
                putExtra("restaurant1", restaurant1.name) // Pass restaurant name
                putExtra("menu1", restaurant1.rMenu)     // Pass menu details
                putExtra("ingredients1", restaurant1.ingredients1.toTypedArray()) // Pass ingredients
                putExtra("price1", restaurant1.menuItemPrice) // Pass price

                putExtra("restaurant2", restaurant2.name)
                putExtra("menu2", restaurant2.rMenu)
                putExtra("ingredients2", restaurant2.ingredients1.toTypedArray())
                putExtra("price2", restaurant2.menuItemPrice)
            }
            startActivity(intent)
        } else {
            // If "Review" is selected, start ReviewComparisonActivity
            val intent = Intent(this, ReviewComparisonActivity::class.java).apply {
                putExtra("restaurant1", restaurant1.name)
                putExtra("review1", restaurant1.restaurantReviews1)
                putExtra("review2", restaurant1.restaurantReviews)

                putExtra("restaurant2", restaurant2.name)
                putExtra("review3", restaurant2.restaurantReviews1)
                putExtra("review4", restaurant2.restaurantReviews)
            }
            startActivity(intent)
        }
    }

    private fun isMenuSimilar(menu1: String, menu2: String): Boolean {
        return stringSimilarity(menu1, menu2) >= 55 // Or whatever threshold you want
    }

}