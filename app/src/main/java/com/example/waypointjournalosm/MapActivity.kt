package com.example.waypointjournalosm

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import android.widget.Button
import android.widget.TextView
import androidx.preference.PreferenceManager
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MapActivity : AppCompatActivity() {

    private lateinit var mapContainer: MapView
    private lateinit var checklistOverlay: RecyclerView
    private lateinit var restaurantDetailsLayout: View
    private lateinit var restaurantNameTextView: TextView
    private lateinit var restaurantMenuTextView: TextView
    private lateinit var toggleChecklistButton: ImageButton
    private lateinit var addToChecklistButton: Button
    private lateinit var checklistAdapter: ChecklistAdapter
    private lateinit var restaurantItem: RestaurantItem
    private lateinit var compareButton: Button
    private lateinit var restaurant1Layout: View
    private lateinit var restaurant2Layout: View
    private lateinit var restaurant1Name: TextView
    private lateinit var restaurant1Menu: TextView
    private lateinit var restaurant1Ingredients: TextView
    private lateinit var restaurant1Reviews: TextView
    private lateinit var restaurant2Name: TextView
    private lateinit var restaurant2Menu: TextView
    private lateinit var restaurant2Ingredients: TextView
    private lateinit var restaurant2Reviews: TextView
    private lateinit var chooseRestaurant1Button: Button
    private lateinit var chooseRestaurant2Button: Button

    private var selectedRestaurant1: RestaurantItem? = null
    private var selectedRestaurant2: RestaurantItem? = null
    private var isComparisonModeEnabled = false
    private var selectedRestaurantMenu: RestaurantData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContentView(R.layout.show_map)

        // Initialize OSMDroid
        Configuration.getInstance().load(this, getSharedPreferences("osm_prefs", MODE_PRIVATE))

        // Set up the MapView
        mapContainer = findViewById(R.id.mapContainer)
        mapContainer.setTileSource(TileSourceFactory.MAPNIK)
        mapContainer.setMultiTouchControls(true)

        // Define the geo-coordinates for Burnham Park
        val burnhamParkCenter = GeoPoint(16.411019, 120.595149)

        // Set the initial view to center on Burnham Park
        mapContainer.controller.setZoom(16.0)
        mapContainer.controller.setCenter(burnhamParkCenter)

        // Restrict the map to a bounding box around Burnham Park
        val boundingBox = BoundingBox(
            16.413479,  // North boundary (lat)
            120.597071, // East boundary (lon)
            16.409221,  // South boundary (lat)
            120.591379  // West boundary (lon)
        )
        mapContainer.setScrollableAreaLimitDouble(boundingBox)

        // Add a marker on Burnham Park
        val marker = Marker(mapContainer)
        marker.position = burnhamParkCenter
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Burnham Park"
        mapContainer.overlays.add(marker)


        // Set click listeners for the icons
        findViewById<ImageView>(R.id.search_icon).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        /*
        findViewById<ImageView>(R.id.icon2).setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.icon3).setOnClickListener {
            val intent = Intent(this, Activity3::class.java)
            startActivity(intent)
        }
         */
        restaurantItem = ViewModelProvider(this)[RestaurantItem::class.java]

        // Initialize checklist RecyclerView and adapter
        checklistOverlay = findViewById(R.id.checklistRecyclerView)
        checklistAdapter = ChecklistAdapter(restaurantItem)
        checklistOverlay.layoutManager = LinearLayoutManager(this)
        checklistOverlay.adapter = checklistAdapter

        // Observe checklist changes
        restaurantItem.checklist.observe(this) {
            checklistAdapter.updateChecklist()  // Notify adapter on checklist change
        }

        restaurantItem.restaurantList.observe(this, Observer { restaurantList ->
            updateMapMarkers(restaurantList)
        })

        // Initialize checklist overlay and toggle button
        toggleChecklistButton = findViewById(R.id.toggleChecklistButton)
        checklistOverlay.visibility = View.GONE // Start with checklist hidden

        toggleChecklistButton.setOnClickListener {
            if (checklistOverlay.visibility == View.GONE) {
                checklistOverlay.visibility = View.VISIBLE
            } else {
                checklistOverlay.visibility = View.GONE
            }
        }
        addToChecklistButton = findViewById(R.id.addToChecklistButton)
        addToChecklistButton.setOnClickListener {
            // Get the current restaurant’s name and add it to the checklist
            val restaurantName = restaurantNameTextView.text.toString()
            if (restaurantName.isNotEmpty()) {
                restaurantItem.addRestaurantToChecklist(restaurantName)
            }
            // Hide the details layout after adding
            restaurantDetailsLayout.visibility = View.GONE
        }

        restaurantDetailsLayout = findViewById(R.id.restaurantDetailsLayout)
        restaurantNameTextView = findViewById(R.id.restaurantName)
        restaurantMenuTextView = findViewById(R.id.restaurantMenu)

        compareButton = findViewById(R.id.compareRestaurantsButton)
        restaurant1Layout = findViewById(R.id.restaurant1Layout)
        restaurant2Layout = findViewById(R.id.restaurant2Layout)
        restaurant1Name = findViewById(R.id.restaurant1Name)
        restaurant1Menu = findViewById(R.id.restaurant1Menu)
        restaurant1Ingredients = findViewById(R.id.restaurant1Ingredients)
        restaurant1Reviews = findViewById(R.id.restaurant1Reviews)
        restaurant2Name = findViewById(R.id.restaurant2Name)
        restaurant2Menu = findViewById(R.id.restaurant2Menu)
        restaurant2Ingredients = findViewById(R.id.restaurant2Ingredients)
        restaurant2Reviews = findViewById(R.id.restaurant2Reviews)
        chooseRestaurant1Button = findViewById(R.id.chooseRestaurant1Button)
        chooseRestaurant2Button = findViewById(R.id.chooseRestaurant2Button)

        // Set up observers for Restaurant 1 details
        selectedRestaurant1?.name?.observe(this, Observer { name ->
            restaurant1Name.text = name ?: "N/A"
        })
        selectedRestaurant1?.rMenu?.observe(this, Observer { menu ->
            restaurant1Menu.text = menu?.joinToString(", ") ?: "N/A"
        })
        selectedRestaurant1?.restaurantRating?.observe(this, Observer { ratings ->
            restaurant1Ingredients.text = (ratings ?: "N/A") as CharSequence?
        })
        selectedRestaurant1?.restaurantReviews?.observe(this, Observer { reviews ->
            restaurant1Reviews.text = reviews ?: "N/A"
        })

        // Set up observers for Restaurant 2 details
        selectedRestaurant2?.name?.observe(this, Observer { name ->
            restaurant2Name.text = name ?: "N/A"
        })
        selectedRestaurant2?.rMenu?.observe(this, Observer { menu ->
            restaurant2Menu.text = menu?.joinToString(", ") ?: "N/A"
        })
        selectedRestaurant2?.restaurantRating?.observe(this, Observer { ratings ->
            restaurant2Ingredients.text = (ratings ?: "N/A") as CharSequence?
        })
        selectedRestaurant2?.restaurantReviews?.observe(this, Observer { reviews ->
            restaurant2Reviews.text = reviews ?: "N/A"
        })

        compareButton.setOnClickListener {
            isComparisonModeEnabled = true
            selectedRestaurant1 = null
            selectedRestaurant2 = null
            Toast.makeText(this, "Select two restaurants to compare", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.chooseRestaurant1Button).setOnClickListener {
            selectedRestaurant1?.let { addRestaurantToChecklist(it.name.value ?: "") }
            resetComparison()
        }

        findViewById<Button>(R.id.chooseRestaurant2Button).setOnClickListener {
            selectedRestaurant2?.let { addRestaurantToChecklist(it.name.value ?: "") }
            resetComparison()
        }

        findViewById<Button>(R.id.compareItemsButton).setOnClickListener {
            selectedRestaurantMenu?.let { restaurant ->
                val intent = Intent(this, CompareMenuItemActivity::class.java).apply {
                    putExtra("SELECTED_RESTAURANT_NAME", restaurant.name)
                    putStringArrayListExtra("SELECTED_RESTAURANT_MENU", ArrayList(restaurant.rMenu))
                    putStringArrayListExtra("SELECTED_MENU_INGREDIENTS1", ArrayList(restaurant.ingredients1))
                    putStringArrayListExtra("SELECTED_MENU_INGREDIENTS2", ArrayList(restaurant.ingredients2))
                    putStringArrayListExtra("SELECTED_MENU_INGREDIENTS3", ArrayList(restaurant.ingredients3))
                    putExtra("SELECTED_MENU_RATING1", restaurant.menuItemRating1)
                    putExtra("SELECTED_MENU_RATING2", restaurant.menuItemRating2)
                    putExtra("SELECTED_MENU_RATING3", restaurant.menuItemRating3)
                    putExtra("SELECTED_MENU_REVIEWS1", restaurant.menuItemReviews1)
                    putExtra("SELECTED_MENU_REVIEWS2", restaurant.menuItemReviews2)
                    putExtra("SELECTED_MENU_REVIEWS3", restaurant.menuItemReviews3)
                }
                startActivity(intent)
            }
        }

    }
    private fun selectRestaurant(restaurantData: RestaurantData) {
        if (!isComparisonModeEnabled) return // Exit if comparison mode is not enabled

        // Convert RestaurantData to RestaurantItem to use LiveData properties
        val restaurantItem = RestaurantItem().apply {
            name.value = restaurantData.name
            rMenu.value = restaurantData.rMenu
            restaurantRating.value = restaurantData.restaurantRating
            restaurantReviews.value = restaurantData.restaurantReviews
        }

        if (selectedRestaurant1 == null) {
            selectedRestaurant1 = restaurantItem
        } else if (selectedRestaurant2 == null) {
            selectedRestaurant2 = restaurantItem
            isComparisonModeEnabled = false // Exit comparison mode after two selections
            displayComparison()
        }
    }

    private fun updateMapMarkers(restaurantList: List<RestaurantData>) {
        // Clear existing markers if needed
        mapContainer.overlays.clear()

        // Loop through restaurantList to create and add markers
        restaurantList.forEach { restaurantData ->
            val geoPoint = GeoPoint(restaurantData.latitude, restaurantData.longitude)
            val marker = Marker(mapContainer)
            marker.position = geoPoint
            marker.title = restaurantData.name
            marker.snippet = restaurantData.rMenu.joinToString(", ")
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            // Show restaurant details and add-to-checklist button on marker tap
            marker.setOnMarkerClickListener { _, _ ->
                restaurantNameTextView.text = restaurantData.name
                restaurantMenuTextView.text = restaurantData.rMenu.joinToString(", ")
                restaurantDetailsLayout.visibility = View.VISIBLE
                selectRestaurant(restaurantData)

                selectedRestaurantMenu = restaurantData

                true // Return true to consume the tap event
            }

            // Add marker to the map
            mapContainer.overlays.add(marker)
        }
        // Refresh the map view to display the markers
        mapContainer.invalidate()
    }

    @SuppressLint("SetTextI18n")
    private fun displayComparison() {
        // Make sure the comparison layouts are visible
        findViewById<View>(R.id.restaurant1Layout).visibility = View.VISIBLE
        findViewById<View>(R.id.restaurant2Layout).visibility = View.VISIBLE

        // Populate Restaurant 1 layout
        findViewById<TextView>(R.id.restaurant1Name).text = selectedRestaurant1?.name?.value ?: "N/A"
        findViewById<TextView>(R.id.restaurant1Menu).text = "Menu: " + (selectedRestaurant1?.rMenu?.value ?.joinToString(", ") ?: "N/A")
        findViewById<TextView>(R.id.restaurant1Ingredients).text = "Rating: " + (selectedRestaurant1?.restaurantRating?.value ?: "N/A")
        findViewById<TextView>(R.id.restaurant1Reviews).text = selectedRestaurant1?.restaurantReviews?.value ?: "N/A"

        // Populate Restaurant 2 layout
        findViewById<TextView>(R.id.restaurant2Name).text = selectedRestaurant2?.name?.value ?: "N/A"
        findViewById<TextView>(R.id.restaurant2Menu).text = "Menu: " + (selectedRestaurant2?.rMenu?.value ?.joinToString(", ") ?: "N/A")
        findViewById<TextView>(R.id.restaurant2Ingredients).text = "Rating: " + (selectedRestaurant2?.restaurantRating?.value ?: "N/A")
        findViewById<TextView>(R.id.restaurant2Reviews).text = selectedRestaurant2?.restaurantReviews?.value ?: "N/A"
    }

    private fun addRestaurantToChecklist(restaurantName: String) {
        // Add to checklist in RestaurantItem
        restaurantItem.checklist.value?.add(restaurantName)
        checklistAdapter.updateChecklist()
    }

    private fun resetComparison() {
        findViewById<View>(R.id.restaurant1Layout).visibility = View.GONE
        findViewById<View>(R.id.restaurant2Layout).visibility = View.GONE
        restaurantDetailsLayout.visibility = View.GONE
        selectedRestaurant1 = null
        selectedRestaurant2 = null
    }

    override fun onResume() {
        super.onResume()
        mapContainer.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapContainer.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapContainer.onDetach() // Release resources
    }

    }






