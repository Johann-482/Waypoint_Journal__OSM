package com.example.waypointjournalosm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize OSMDroid
        Configuration.getInstance().load(this, getSharedPreferences("osm_prefs", MODE_PRIVATE))

        // Set up the MapView
        mapView = findViewById(R.id.osm_map)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Define the geo-coordinates for Burnham Park
        val burnhamParkCenter = GeoPoint(16.411019, 120.595149)

        // Set the initial view to center on Burnham Park
        mapView.controller.setZoom(16.0)
        mapView.controller.setCenter(burnhamParkCenter)

        // Restrict the map to a bounding box around Burnham Park
        val boundingBox = BoundingBox(
            16.413479,  // North boundary (lat)
            120.597071, // East boundary (lon)
            16.409221,  // South boundary (lat)
            120.591379  // West boundary (lon)
        )
        mapView.setScrollableAreaLimitDouble(boundingBox)

        // Add a marker on Burnham Park
        val marker = Marker(mapView)
        marker.position = burnhamParkCenter
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Burnham Park"
        mapView.overlays.add(marker)

        /* Set click listeners for the icons
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            val intent = Intent(this, Activity1::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.icon2).setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.icon3).setOnClickListener {
            val intent = Intent(this, Activity3::class.java)
            startActivity(intent)
        }
         */
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
