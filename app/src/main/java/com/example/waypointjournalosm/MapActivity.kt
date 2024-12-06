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
import android.widget.Button
import android.widget.TextView
import androidx.preference.PreferenceManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MapActivity : AppCompatActivity() {

    private lateinit var mapContainer: MapView
    private lateinit var restaurantDetailsLayout: View
    private lateinit var restaurantNameTextView: TextView
    private lateinit var restaurantMenuTextView: TextView
    private lateinit var addToChecklistButton: Button
    private lateinit var restaurantItem: RestaurantItem

    private var selectedRestaurantMenu: RestaurantData? = null
    private var selectedRestaurantName: String? = null

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
            16.42901,  // North boundary (lat)
            120.61152, // East boundary (lon)
            16.39640,  // South boundary (lat)
            120.57701  // West boundary (lon)
        )
        mapContainer.setScrollableAreaLimitDouble(boundingBox)

        // Add a marker on Burnham Park
        val marker = Marker(mapContainer)
        marker.position = burnhamParkCenter
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Burnham Park"
        mapContainer.overlays.add(marker)

        val search = findViewById<Button>(R.id.search_restaurant)


        // Set click listeners for the icons
        search.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        restaurantItem = ViewModelProvider(this)[RestaurantItem::class.java]

        restaurantItem.restaurantList.observe(this, Observer { restaurantList ->
            updateMapMarkers(restaurantList)
        })

        addToChecklistButton = findViewById(R.id.addToChecklistButton)

        // Add the listener for the Add to Checklist button
        addToChecklistButton.setOnClickListener {
            // Show the itinerary dialog only if a restaurant has been selected
            if (!selectedRestaurantName.isNullOrEmpty()) {
                showItineraryDialog(selectedRestaurantName!!)
            } else {
                Toast.makeText(this, "Please select a restaurant first.", Toast.LENGTH_SHORT).show()
            }
        }

        restaurantDetailsLayout = findViewById(R.id.restaurantDetailsLayout)
        restaurantNameTextView = findViewById(R.id.restaurantName)
        restaurantMenuTextView = findViewById(R.id.restaurantMenu)

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
            marker.snippet = restaurantData.rMenu
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            // Show restaurant details and add-to-checklist button on marker tap
            marker.setOnMarkerClickListener { _, _ ->
                selectedRestaurantName = restaurantData.name // Store the selected restaurant name
                restaurantNameTextView.text = restaurantData.name
                restaurantMenuTextView.text = restaurantData.rMenu
                restaurantDetailsLayout.visibility = View.VISIBLE

                selectedRestaurantMenu = restaurantData

                true // Return true to consume the tap event
            }

            // Add marker to the map
            mapContainer.overlays.add(marker)
        }
        // Refresh the map view to display the markers
        mapContainer.invalidate()
    }


    private fun showItineraryDialog(restaurantName: String) {
        val dialogFragment = ItineraryDialogFragment.newInstance(restaurantName)
        dialogFragment.show(supportFragmentManager, "ItineraryDialog")
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
