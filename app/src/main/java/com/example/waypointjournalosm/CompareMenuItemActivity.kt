package com.example.waypointjournalosm

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView


class CompareMenuItemActivity  : AppCompatActivity() {
    private val restaurantItem: RestaurantItem by viewModels()
    private lateinit var compareListAdapter: CompareListAdapter
    private var selectedMenuItem: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compare_item)

        compareListAdapter = CompareListAdapter(
            onAddClick = { item ->
                selectedMenuItem?.let { menuItem ->
                    handleCompare(item, menuItem)
                } ?: Toast.makeText(this, "No menu item selected", Toast.LENGTH_SHORT).show()
            },
            onInfoClick = { item ->
            }
        )

        val recyclerView = findViewById<RecyclerView>(R.id.compareRestaurantRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = compareListAdapter

        restaurantItem.restaurantList.observe(this, Observer { restaurantList ->
            compareListAdapter.submitList(restaurantList)
        })

        val restaurantName = intent.getStringExtra("SELECTED_RESTAURANT_NAME")
        val restaurantMenu = intent.getStringArrayListExtra("SELECTED_RESTAURANT_MENU")
        val ingredients1 = intent.getStringArrayListExtra("SELECTED_MENU_INGREDIENTS1")
        val ingredients2 = intent.getStringArrayListExtra("SELECTED_MENU_INGREDIENTS2")
        val ingredients3 = intent.getStringArrayListExtra("SELECTED_MENU_INGREDIENTS3")
        val menuItemRating1 = intent.getDoubleExtra("SELECTED_MENU_RATING1", 0.0)
        val menuItemRating2 = intent.getDoubleExtra("SELECTED_MENU_RATING2", 0.0)
        val menuItemRating3 = intent.getDoubleExtra("SELECTED_MENU_RATING3", 0.0)
        val menuItemReview1 = intent.getStringExtra("SELECTED_MENU_REVIEWS1")
        val menuItemReview2 = intent.getStringExtra("SELECTED_MENU_REVIEWS2")
        val menuItemReview3 = intent.getStringExtra("SELECTED_MENU_REVIEWS3")

        val ingredientsTextView = findViewById<TextView>(R.id.restaurantItemIngredients1)
        val ratingTextView = findViewById<TextView>(R.id.restaurantMenuRating1)
        val reviewTextView = findViewById<TextView>(R.id.restaurantItemReview1)

        // Populate the top layout
        findViewById<TextView>(R.id.restaurantName1).text = restaurantName

        // Dynamically populate buttons with menu items
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)

        restaurantMenu?.let { menu ->
            if (menu.isNotEmpty()) {
                button4.text = menu.getOrNull(0) ?: "Item 1"
                button5.text = menu.getOrNull(1) ?: "Item 2"
                button6.text = menu.getOrNull(2) ?: "Item 3"
            } else {
                // If the menu is empty, set default text
                button4.text = "No Item"
                button5.text = "No Item"
                button6.text = "No Item"
            }
        }

        // Set up button click listeners to filter the restaurant list
        val restaurantList = restaurantItem.restaurantList.value ?: emptyList()

        val handleButtonClick = { index: Int, menuItem: String ->
            // Update TextViews
            selectedMenuItem = menuItem
            when (index) {
                0 -> {
                    ingredientsTextView.text = ("Ingredients: " + ingredients1?.joinToString(", "))
                    ratingTextView.text = "Rating: $menuItemRating1"
                    reviewTextView.text = "Reviews: \n$menuItemReview1"
                }
                1 -> {
                    ingredientsTextView.text = ("Ingredients: " + ingredients2?.joinToString(", "))
                    ratingTextView.text = "Rating: $menuItemRating2"
                    reviewTextView.text = "Reviews: \n$menuItemReview2"
                }
                2 -> {
                    ingredientsTextView.text = ("Ingredients: " + ingredients3?.joinToString(", "))
                    ratingTextView.text = "Rating: $menuItemRating3"
                    reviewTextView.text = "Reviews: \n$menuItemReview3"
                }
            }

            // Filter and display restaurants serving the selected menu item
            val filteredList = restaurantList.filter { it.rMenu.contains(menuItem) }
            compareListAdapter.submitList(filteredList)

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No restaurants serve $menuItem", Toast.LENGTH_SHORT).show()
            }
        }

        button4.setOnClickListener {
            handleButtonClick(0, button4.text.toString())
        }

        button5.setOnClickListener {
            handleButtonClick(1, button5.text.toString())
        }

        button6.setOnClickListener {
            handleButtonClick(2, button6.text.toString())
        }

    }

    private fun handleCompare(item: RestaurantData, menuItem: String) {
        val menuIndex = item.rMenu.indexOf(menuItem)
        if (menuIndex != -1) {
            val ingredients = when (menuIndex) {
                0 -> item.ingredients1
                1 -> item.ingredients2
                2 -> item.ingredients3
                else -> emptyList()
            }
            val rating = when (menuIndex) {
                0 -> item.menuItemRating1
                1 -> item.menuItemRating2
                2 -> item.menuItemRating3
                else -> 0.0
            }
            val reviews = when (menuIndex) {
                0 -> item.menuItemReviews1
                1 -> item.menuItemReviews2
                2 -> item.menuItemReviews3
                else -> "No reviews available"
            }

            // Update bottom layout with selected restaurant's data
            findViewById<TextView>(R.id.restaurantName2).text = item.name
            findViewById<TextView>(R.id.restaurantMenuItem2).text = menuItem
            findViewById<TextView>(R.id.restaurantItemIngredients2).text = ingredients.joinToString(", ")
            findViewById<TextView>(R.id.restaurantMenuRating2).text = "Rating: $rating"
            findViewById<TextView>(R.id.restaurantItemReview2).text = reviews
        } else {
            Toast.makeText(this, "Selected menu item not found in this restaurant", Toast.LENGTH_SHORT).show()
        }
    }
}