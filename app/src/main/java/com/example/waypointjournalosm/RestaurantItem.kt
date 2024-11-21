package com.example.waypointjournalosm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RestaurantData(
    val name: String,
    val rMenu: List<String>,
    val ingredients1: List<String>,
    val ingredients2: List<String>,
    val ingredients3: List<String>,
    val menuItemRating1: Double,
    val menuItemRating2: Double,
    val menuItemRating3: Double,
    val restaurantRating: Double,
    val menuItemReviews1: String,
    val menuItemReviews2: String,
    val menuItemReviews3: String,
    val restaurantReviews: String,
    val latitude: Double,
    val longitude: Double
)

class RestaurantItem : ViewModel() {
    // Private MutableLiveData to hold the restaurant list
    private val _restaurantList = MutableLiveData<List<RestaurantData>>()

    val name = MutableLiveData<String>()
    val rMenu = MutableLiveData<List<String>>()
    val restaurantRating = MutableLiveData<Double>()
    val restaurantReviews = MutableLiveData<String>()

    // Public LiveData for observers to access the data
    val restaurantList: LiveData<List<RestaurantData>> get() = _restaurantList

    // LiveData for user-selected checklist of restaurant names
    private val _checklist = MutableLiveData<MutableList<String>>(mutableListOf())
    val checklist: LiveData<MutableList<String>> get() = _checklist

    // Initialize the restaurant data (could be loaded from a repository)
    init {
        _restaurantList.value = listOf(
            RestaurantData("Restaurant NumberA", listOf("Pizza", "Pasta", "Salad"),
                listOf("Flour", "Yeast", "Tomato sauce", "Cheese", "Garlic"),
                listOf("Olive oil", "Tomato paste", "Salt", "Basil", "Parsley"),
                listOf("split pea", "peaches", "artificial sweetener", "hazelnut", "blueberry"),
                4.1, 3.9, 4.4, 4.2, "ReviewItem1.1", "ReviewItem1.2", "ReviewItem1.3",
                "Review: This place is good." , 16.411119, 120.595190),
            RestaurantData("Restaurant NumberB", listOf("Sushi", "Ramen", "Pizza"),
                listOf("Pea", "Ravioli", "Cabbage", "Lentil", "Olive"),
                listOf("summer squash", "mayonnaise", "cashew nut", "black bean", "kale"),
                listOf("apple butter", "cranberry", "maize", "buckwheat", "jelly"),
                4.5, 4.9, 4.2, 4.6, "ReviewItem2.1", "ReviewItem2.2", "ReviewItem2.3",
                "Review: This place is awesome.", 16.410719, 120.595100),
            RestaurantData("Restaurant NumberC", listOf("Pasta", "Chicken", "Ramen"),
                listOf("creme fraiche", "vanilla bean", "crabs", "anchovy paste", "monkfish"),
                listOf("pumpkin seed", "almond paste", "watermelon", "date sugar", "soy ice cream"),
                listOf("rye", "kidney bean", "wine", "quiche", "Kahlua"), 3.8, 4.3, 4.6, 4.7,
                "ReviewItem3.1", "ReviewItem3.2", "ReviewItem3.3",
                "Review: This place is, well, something...", 16.413185, 120.593578)
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