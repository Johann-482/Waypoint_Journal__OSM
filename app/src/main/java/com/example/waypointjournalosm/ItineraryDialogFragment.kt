package com.example.waypointjournalosm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ItineraryDialogFragment : DialogFragment() {

    private lateinit var firebaseDatabase: DatabaseReference
    private var restaurantName: String? = null
    private var selectedOption: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overlay_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe context initialization
        val safeContext = context ?: return

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Itinerary")


        // Retrieve restaurant name
        restaurantName = arguments?.getString("restaurantName")
        val restaurantNameTextView: TextView = view.findViewById(R.id.textView2)
        restaurantNameTextView.text = restaurantName

        // Set up dropdown
        val options = listOf("Breakfast", "Lunch", "Dinner")
        val autoCompleteTextView: AutoCompleteTextView = view.findViewById(R.id.auto_complete_text)
        val adapter = ArrayAdapter(safeContext, android.R.layout.simple_dropdown_item_1line, options)
        autoCompleteTextView.setAdapter(adapter)

        // Track selected option
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedOption = options[position]
        }

        // Button logic
        view.findViewById<Button>(R.id.button).setOnClickListener {
            if (!selectedOption.isNullOrEmpty() && !restaurantName.isNullOrEmpty()) {
                saveToFirebase(restaurantName!!, selectedOption!!)
            } else {
                Toast.makeText(safeContext, "Please select the time of meal.", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.button2).setOnClickListener {
            dismiss()
            Toast.makeText(safeContext, "Action canceled.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val maxWidth = (resources.displayMetrics.widthPixels * 1).toInt() // 90% of screen width
        val maxHeight = (resources.displayMetrics.heightPixels * 1).toInt() // Wrap content height
        dialog.window?.setLayout(
            maxWidth.coerceAtMost(1100), // Max width (800px as an example)
            maxHeight.coerceAtMost(850) // Max height (600px as an example)
        )
    }

    private fun saveToFirebase(restaurantName: String, mealType: String) {
        val itineraryItem = ItineraryItem(restaurantName, mealType)
        firebaseDatabase.push().setValue(itineraryItem)
            .addOnSuccessListener {
                if (isAdded) {
                    Toast.makeText(requireContext(), "$restaurantName for $mealType saved successfully!", Toast.LENGTH_SHORT).show()
                    dismiss() // Dismiss fragment after the Toast is shown
                }
            }
            .addOnFailureListener {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to save $restaurantName.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        fun newInstance(restaurantName: String): ItineraryDialogFragment {
            val fragment = ItineraryDialogFragment()
            val args = Bundle()
            args.putString("restaurantName", restaurantName)
            fragment.arguments = args
            return fragment
        }
    }
}