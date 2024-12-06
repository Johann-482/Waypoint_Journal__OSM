package com.example.waypointjournalosm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RestaurantData(
    val name: String,
    val rMenu: String,
    val ingredients1: List<String>,
    val menuItemRating1: Int,
    val restaurantRating: Int,
    val restaurantReviews1: String,
    val restaurantReviews: String,
    val menuItemPrice: Int,
    val latitude: Double,
    val longitude: Double
)

class RestaurantItem : ViewModel() {
    // Private MutableLiveData to hold the restaurant list
    private val _restaurantList = MutableLiveData<List<RestaurantData>>()

    val name = MutableLiveData<String>()
    val rMenu = MutableLiveData<String>()
    val restaurantRating = MutableLiveData<Int>()
    val restaurantReviews = MutableLiveData<String>()

    // Public LiveData for observers to access the data
    val restaurantList: LiveData<List<RestaurantData>> get() = _restaurantList

    // LiveData for user-selected checklist of restaurant names
    private val _checklist = MutableLiveData<MutableList<ItineraryItem>>(mutableListOf())
    val checklist: LiveData<MutableList<ItineraryItem>> get() = _checklist

    // Initialize the restaurant data (could be loaded from a repository)
    init {
        _restaurantList.value = listOf(
            RestaurantData("Cafe by the Ruins","Pinikpikan Chicken",
                listOf("Chicken", "Bokchoy", "Etag", "Ginger"),
                3, 4, "My colleagues and I dined here during an event in Baguio, and it was my first time visiting. I was pleasantly surprised by how affordable the menu options were.",
                "That kamote bread is unusual but really good with white cheese. I only tried this, plus iced coffee and egg, so definitely need to go back to try their signature dishes.",
                350, 16.413129716981057, 120.5916646888029),

            RestaurantData("Pet's Bulaluhan","Pinikpikan (Half)",
                listOf("Half Chicken", "Cabbage", "Calamansi"),
                4, 3, "Food is piping hot, it's perfect for the Baguio weather. Plenty of local dishes to choose from.",
                "Okay pa rin naman ung nguso-liver. Most of the items in the menu are not available on my visit. Area is not well-maintained.",
                90, 16.410249728174726, 120.59868336661759),

            RestaurantData("Simply Special Restaurant","Pinikpikan",
                listOf("Chicken", "Chayote", "Ginger"),
                2, 3, "Simply Special Restaurant delivers a unique culinary experience with its creative dishes that blend local flavors with contemporary twists, although the inconsistent service and limited menu options may leave some diners wanting more",
                "Authentic Filipino dishes, attentive service, and a cozy atmosphere, making it a must-visit for anyone seeking local flavors in the heart of the city.",
                150, 16.412888149617444, 120.59609079279537),

            RestaurantData("Point & Grill","Pinikpikan",
                listOf("Chicken", "Bokchoy", "Etag"),
                2, 4, "Foods are great, worth the price for their big servings. Some staff were not just so friendly.",
                "Delicious affordable food. Service is from slow to fast depending on how you get the staffs attention",
                160, 16.41221564439065, 120.5979275841993),

            RestaurantData("Good Taste","Strawberry Sinigang",
                listOf("pork belly", "strawberries", "tamarind paste", "kangkong"),
                4, 4, "Food is definitely great. Good serving amount. First time to see a robot server also.",
                "I was surprised by how delicious the food was! The portion sizes were generous, easily enough for two people. While the atmosphere was lively and busy, we still had a great time.",
                360, 16.412000176610633, 120.59205718195771),

            RestaurantData("Kulinarista","Strawberry Sinigang",
                listOf("salmon", "strawberries", "guava", "eggplant"),
                3, 3, "Wow this place is such a treasure!! Food is absolutely delicious and the presentation was gorgeous! We ordered the Tuna, pork ribs and the Lechon Manok.",
                "Was looking for a place to have breakfast with a view in Baguio when I chanced on this place along Legarda st. Near Orchard Hotel and Holiday Inn.",
                170, 16.406652387534614, 120.59313588195765),

            RestaurantData("Kuya J Restaurant","Strawberry Sinigang",
                listOf("shrimp", "strawberries", "calamansi juice", "water spinach"),
                4, 3, "It's cool here guys",
                "Kuya J Restaurant offers a variety of local dishes that highlight the rich culinary heritage of the Philippines. The menu features traditional Filipino favorites that are often well-received by both locals and tourists. ",
                210, 16.409505661608296, 120.5987692224424),

            RestaurantData("LE HOMI'S CUCINA","Strawberry Sinigang",
                listOf("pork belly", "strawberries", "tamarind paste", "kangkong"),
                2, 3, "LE HOMI'S CUCINA in Baguio offers a variety of comforting Italian dishes, but while the flavors are satisfying, the inconsistent service and occasional long wait times can detract from the overall dining experience.",
                "Dishes like their classic lasagna and hearty risottos, the overall service feels a bit lacking, sometimes leaving diners waiting longer than expected.",
                140, 16.410297319915077, 120.59878799545275),

            RestaurantData("Ai Filipino Korean Buffet","Binungor",
                listOf("pork", "taro", "coconut milk", "ginger"),
                3, 3, "It is highly recommended especially if you love shabu shabu, filipino dishes and fresh seafood and meat! Especially their hotpot tastes superb.",
                "I highly recommend it to groups of men or boys. Win with the amount of NEAT and begins choices, plus the shrimp and squid plus again if you also love Filipino food.",
                180, 16.408666197268893, 120.59189583593748),

            RestaurantData("Dap-Ay Restaurant","Binungor",
                listOf("shrimp", "squash", "ginger", "chili peppers"),
                4, 2, "Kare-Kare and Adobo, which are flavorful and hearty.",
                "Occasional lapses in service and a lack of ambiance that could create a more inviting atmosphere.",
                230, 16.413522233265244, 120.59518243962786),

            RestaurantData("Rito's Restaurant 2","Binungor",
                listOf("eggplant", "green beans", "coconut milk", "chickpeas"),
                3, 3, "Cozy spot that serves flavorful local favorites like their famous Baguio-style longganisa.",
                "The service can be slow during peak hours, which detracts from an otherwise delightful dining experience.",
                160, 16.411414401465567, 120.5987536936075),

            RestaurantData("Pamana","Binungor",
                listOf("ground beef", "sweet potatoes", "spinach", "garlic"),
                3, 3, "Accessible, cozy, spacious. Comfort food in big servings. Warm staff.",
                "Honestly, it's one of the best Filipino restaurants in the city. All of our orders were delicious, especially the Lumpia Trio. Also, the food servings were ample.",
                190, 16.409114537466394, 120.60070466846287),

            RestaurantData("Ili-Likha Artists Village","Pinuneg",
                listOf("pig’s blood", "minced pork fat", "salt", "red onions"),
                3, 3, "We ended up dining in Cafe Cueva. Love the ambiance inside.",
                "you got to choose which cafe/shop to eat and buy some of their handcrafts! and foods are amazing. got to meet their cat.",
                180, 16.41402219915432, 120.59718986661773),

            RestaurantData("Foodtrip in Tandem","Pinuneg",
                listOf("pig’s blood", "minced pork fat", "salt", "red onions"),
                2, 2, "The lackluster flavors and poorly executed dishes make it hard to recommend this restaurant for anyone seeking authentic Baguio cuisine.",
                "I had high hopes for Foodtrip in Tandem, but the uninspired dishes and inattentive staff turned what should have been a delightful meal into a forgettable outing.",
                120, 16.41439511557545, 120.5947409954528),

            RestaurantData("Jack's Baguio Restaurant","Pinuneg",
                listOf("pig’s blood", "garlic", "chili flakes", "ginger"),
                4, 3, "One of the most sulit meal I have eaten in Baguio along Session Road. I ordered the Jack’s rice. Serving is big enough to fill your tummy.",
                "This is supposed to be “The Best” when it comes to specialty rice. However, its not the same as in the past. There is no originality on their dish and it seems like they are now the ones copying from other restaurants.",
                240, 16.41227171782428, 120.59784116846288),

            RestaurantData("Solibao","Pinuneg",
                listOf("pig's blood", "leeks", "black pepper"),
                3, 3, "Serving is more than enough for 2pax, taste is awesome and their prices are definitely worth it. The place is not too crowded",
                "Visited this restaurant for the first time, and I am not sure if I am coming back again. Food is not cheap but it is not very good.",
                160, 16.410859102838806, 120.59620495312268),
        )
    }
    // Function to convert RestaurantData to ItineraryItem
    private fun convertToItineraryItems(restaurantList: List<RestaurantData>): List<ItineraryItem> {
        return restaurantList.map {
            ItineraryItem(it.name, it.rMenu) // Assume you want the restaurant name and the menu item
        }
    }
    // Function to update the checklist with a list of ItineraryItems
    fun updateChecklist(newRestaurantList: List<RestaurantData>) {
        _checklist.value = convertToItineraryItems(newRestaurantList).toMutableList()
    }

    // Function to filter restaurants by menu based on query
    fun filterRestaurantsByMenu(query: String): List<RestaurantData> {
        return _restaurantList.value?.filter { restaurant ->
            restaurant.rMenu.contains(query, ignoreCase = true)
        } ?: emptyList()
    }

}