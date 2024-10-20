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

class MapActivity : AppCompatActivity() {

    private lateinit var mapContainer: MapView
    private lateinit var checklistOverlay: RecyclerView
    private lateinit var restaurantDetailsLayout: View
    private lateinit var restaurantNameTextView: TextView
    private lateinit var restaurantMenuTextView: TextView
    private lateinit var addToChecklistButton: Button
    private lateinit var toggleChecklistButton: ImageButton
    private val checklistAdapter = ChecklistAdapter()

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

        // Add markers to the map
        addMarkersToMap()

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
        checklistOverlay = findViewById(R.id.checklistRecyclerView)

        checklistOverlay.layoutManager = LinearLayoutManager(this)
        checklistOverlay.adapter = checklistAdapter


        toggleChecklistButton = findViewById(R.id.toggleChecklistButton)
        checklistOverlay.visibility = View.GONE
        toggleChecklistButton.setOnClickListener {
            if (checklistOverlay.visibility == View.GONE) {
                checklistOverlay.visibility = View.VISIBLE
            } else {
                checklistOverlay.visibility = View.GONE
            }
        }
        // Hide restaurant details when user adds the restaurant to the checklist
        addToChecklistButton = findViewById(R.id.addToChecklistButton)
        addToChecklistButton.setOnClickListener {
            val restaurantName = restaurantNameTextView.text.toString()
            checklistAdapter.addRestaurantToChecklist(restaurantName)
            restaurantDetailsLayout.visibility = View.GONE
        }

        restaurantDetailsLayout = findViewById(R.id.restaurantDetailsLayout)
        restaurantNameTextView = findViewById(R.id.restaurantName)
        restaurantMenuTextView = findViewById(R.id.restaurantMenu)



    }
    private fun addMarkersToMap() {
        val marker1 = Marker(mapContainer)
        marker1.position = GeoPoint(16.411119, 120.595190) // Example location
        marker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker1.title = "Restaurant A"
        marker1.snippet = "Pizza, Pasta, Salad"
        marker1.setOnMarkerClickListener { marker, _ ->
            showRestaurantDetails(marker)
            true
        }

        val marker2 = Marker(mapContainer)
        marker2.position = GeoPoint(16.410719, 120.595100) // Example location
        marker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker2.title = "Restaurant B"
        marker2.snippet = "Sushi, Ramen, Tempura"
        marker2.setOnMarkerClickListener { marker, _ ->
            showRestaurantDetails(marker)
            true
        }

        mapContainer.overlays.add(marker1)
        mapContainer.overlays.add(marker2)
    }

    private fun showRestaurantDetails(marker: Marker) {
        // Show the restaurant's details in the top layout
        restaurantNameTextView.text = marker.title
        restaurantMenuTextView.text = marker.snippet
        restaurantDetailsLayout.visibility = View.VISIBLE
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






