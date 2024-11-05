package com.example.waypointjournalosm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RestaurantData(
    val name: String,
    val rMenu: String,
    val latitude: Double,
    val longitude: Double
)

class RestaurantItem : ViewModel() {
    // Private MutableLiveData to hold the restaurant list
    private val _restaurantList = MutableLiveData<List<RestaurantData>>()

    // Public LiveData for observers to access the data
    val restaurantList: LiveData<List<RestaurantData>> get() = _restaurantList

    // LiveData for user-selected checklist of restaurant names
    private val _checklist = MutableLiveData<MutableList<String>>(mutableListOf())
    val checklist: LiveData<MutableList<String>> get() = _checklist

    // Initialize the restaurant data (could be loaded from a repository)
    init {
        _restaurantList.value = listOf(
            RestaurantData("Restaurant A", "Pizza, Pasta, Salad", 16.411119, 120.595190),
            RestaurantData("Restaurant B", "Sushi, Ramen, Tempura", 16.410719, 120.595100)
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