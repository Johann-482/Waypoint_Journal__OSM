package com.example.waypointjournalosm

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
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.waypointjournalosm.RestaurantItem

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
    private var selectedRestaurants = mutableListOf<RestaurantItem>()
    private var isComparisonMode = false // Flag to track if we're in comparison mode

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



    }
    private fun updateMapMarkers(restaurantList: List<RestaurantData>) {
        // Clear existing markers if needed
        mapContainer.overlays.clear()

        // Loop through restaurantList to create and add markers
        restaurantList.forEach { restaurant ->
            val geoPoint = GeoPoint(restaurant.latitude, restaurant.longitude)
            val marker = Marker(mapContainer)
            marker.position = geoPoint
            marker.title = restaurant.name
            marker.snippet = restaurant.rMenu
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            // Show restaurant details and add-to-checklist button on marker tap
            marker.setOnMarkerClickListener { _, _ ->
                restaurantNameTextView.text = restaurant.name
                restaurantMenuTextView.text = restaurant.rMenu
                restaurantDetailsLayout.visibility = View.VISIBLE

                true // Return true to consume the tap event
            }

            // Add marker to the map
            mapContainer.overlays.add(marker)
        }
        // Refresh the map view to display the markers
        mapContainer.invalidate()
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






