package com.example.waypointjournalosm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class JournalActivity : androidx.activity.ComponentActivity() {

    private lateinit var restaurantTabAdapter: RestaurantTabAdapter
    private lateinit var restaurantImageAdapter: ImageAdapter
    private lateinit var tempImageUri: Uri
    private lateinit var restaurantNames: List<String>
    private lateinit var restaurantList: List<RestaurantData> // From RestaurantItem.kt
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.journal_function)

        val rvRestaurantTabs = findViewById<RecyclerView>(R.id.rvRestaurantTabs)
        val tvRestaurantName = findViewById<TextView>(R.id.tvRestaurantName)
        val rvSavedImages = findViewById<RecyclerView>(R.id.rvSavedImages)

        // Get restaurant data (replace with ViewModel or repository logic)
        val restaurantItem = ViewModelProvider(this)[RestaurantItem::class.java]
        restaurantList = restaurantItem.restaurantList.value ?: emptyList()

        // Extract restaurant names
        val restaurantNames = restaurantList.map { it.name }

        // Set up the RestaurantTabAdapter
        restaurantTabAdapter = RestaurantTabAdapter(restaurantNames) { selectedRestaurant ->
            // Update tvRestaurantName when a button is clicked
            tvRestaurantName.text = selectedRestaurant

            // Dynamically update rvSavedImages based on selected restaurant
            updateSavedImagesForRestaurant(selectedRestaurant, rvSavedImages)
        }
        rvRestaurantTabs.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvRestaurantTabs.adapter = restaurantTabAdapter

        // Initialize restaurantImageAdapter with an empty list
        restaurantImageAdapter = ImageAdapter(mutableListOf())

        // Set the ImageAdapter for the RecyclerView
        rvSavedImages.layoutManager = GridLayoutManager(this, 3) // 3 columns for images
        rvSavedImages.adapter = restaurantImageAdapter

        val btnCamera = findViewById<ImageView>(R.id.cameraButton)

        btnCamera.setOnClickListener {
            openCamera()
        }
        // Initialize the permission launcher
        cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, now launch the camera
                launchCameraIntent()
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateSavedImagesForRestaurant(restaurantName: String, rvSavedImages: RecyclerView) {
        val storageRef = Firebase.storage.reference.child("images/$restaurantName")
        val imageUrls = mutableListOf<String>()

        storageRef.listAll().addOnSuccessListener { result ->
            if (result.items.isEmpty()) {
                // If the folder is empty, clear the images
                restaurantImageAdapter.clearImages()
                rvSavedImages.visibility = View.GONE // Optionally hide the RecyclerView
            } else {
                result.items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        imageUrls.add(uri.toString())
                        if (imageUrls.size == result.items.size) {
                            // Update RecyclerView with new images
                            restaurantImageAdapter.updateImages(imageUrls)
                            rvSavedImages.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
            // Handle errors (e.g., folder does not exist)
            Log.e("JournalActivity", "Error fetching images: ${exception.message}")
            restaurantImageAdapter.clearImages() // Clear images if there's an error
            rvSavedImages.visibility = View.GONE // Optionally hide the RecyclerView
        }

    }
    private val CAMERA_REQUEST_CODE = 100
    private fun openCamera() {
        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, open the camera
            launchCameraIntent()
        } else {
            // Request the permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    private fun launchCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Show a prompt to save the picture
            showSavePrompt(imageBitmap)
        }
    }


    private fun showSavePrompt(imageBitmap: Bitmap) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Save Picture")
        builder.setMessage("Do you want to save this picture?")
        builder.setPositiveButton("Yes") { _, _ ->
            showRestaurantListDialog(imageBitmap)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
    private fun showRestaurantListDialog(imageBitmap: Bitmap) {
        // Extract restaurant names from restaurantList
        val restaurantNames = restaurantList.map { it.name }

        // Display restaurant names in a dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a Restaurant")
        builder.setItems(restaurantNames.toTypedArray()) { _, which ->
            val selectedRestaurant = restaurantNames[which]
            // Save image to Firebase under the selected restaurant
            uploadImageToFirebase(imageBitmap, selectedRestaurant)
        }
        builder.show()
    }

    private fun uploadImageToFirebase(imageBitmap: Bitmap, restaurantName: String) {
        // Convert bitmap to byte array for upload
        val storageRef = Firebase.storage.reference.child("images/$restaurantName/${System.currentTimeMillis()}.jpg")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // Upload image to Firebase
        storageRef.putBytes(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Image uploaded to $restaurantName", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 1001
    }
}