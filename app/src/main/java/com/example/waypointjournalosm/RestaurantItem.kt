package com.example.waypointjournalosm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RestaurantData(
    val name: String,
    val rMenu: String,
    val mainIngredients: String,
    val reviews: String,
    val latitude: Double,
    val longitude: Double
)

class RestaurantItem : ViewModel() {
    // Private MutableLiveData to hold the restaurant list
    private val _restaurantList = MutableLiveData<List<RestaurantData>>()

    val name = MutableLiveData<String>()
    val rMenu = MutableLiveData<String>()
    val mainIngredients = MutableLiveData<String>()
    val reviews = MutableLiveData<String>()

    // Public LiveData for observers to access the data
    val restaurantList: LiveData<List<RestaurantData>> get() = _restaurantList

    // LiveData for user-selected checklist of restaurant names
    private val _checklist = MutableLiveData<MutableList<String>>(mutableListOf())
    val checklist: LiveData<MutableList<String>> get() = _checklist

    // Initialize the restaurant data (could be loaded from a repository)
    init {
        _restaurantList.value = listOf(
            RestaurantData("Restaurant NumberA", "Pizza, Pasta, Salad", "Spud, Garlic, Chipotle Pepper, Wafer", "This place is good." , 16.411119, 120.595190),
            RestaurantData("Restaurant NumberB", "Sushi, Ramen, Tempura", "Pea, Ravioli, Cabbage, Lentil, Olive", "This place is awesome", 16.410719, 120.595100)
        )
    }
    // Function to add restaurant name to checklist if not already added
    fun addRestaurantToChecklist(restaurantName: String) {
        _checklist.value?.let {
            if (!it.contains(restaurantName)) {
                it.add(restaurantName)
                _checklist.value = it // Update LiveData to notify observers
            }
        }
    }
}