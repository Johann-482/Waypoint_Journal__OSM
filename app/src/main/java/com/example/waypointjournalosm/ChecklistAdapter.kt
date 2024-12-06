package com.example.waypointjournalosm


import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ChecklistAdapter(
    private val context: Context,
    private val checklist: MutableList<ItineraryItem> // Directly pass the checklist from Activity/Fragment){}
) : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {

    private var checklistData: MutableList<ItineraryItem> = checklist.toMutableList() // Convert to MutableList

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val restaurantNameTextView: TextView = itemView.findViewById(R.id.restaurantNameChecklist)
        val mealTypeTextView: TextView = itemView.findViewById(R.id.textView6)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        // Bind data to views
        fun bind(item: ItineraryItem) {
            restaurantNameTextView.text = item.restaurantName
            mealTypeTextView.text = item.mealType // Show the meal type
            checkBox.isChecked = false // Set default state for the checkbox

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Toast.makeText(itemView.context, "${item.restaurantName} selected! Item will remove in 20 secs...", Toast.LENGTH_SHORT).show()

                    // Trigger delay of 30 seconds and then delete the item
                    Handler().postDelayed({
                        // Perform the deletion after 30 seconds
                        deleteItemFromFirebase(item)
                        // Remove from the local list
                        checklist.remove(item)
                        // Notify the adapter to remove the item from RecyclerView
                        notifyDataSetChanged()
                    }, 20000)  // 30 seconds delay
                }
            }
        }
    }

    private val checkedStateMap = mutableMapOf<String, Boolean>() // Map to hold checked states

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checklist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = checklist[position]
        holder.bind(item)  // Bind data

    }

    override fun getItemCount(): Int = checklist.size

    private fun deleteItemFromFirebase(item: ItineraryItem) {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("Itinerary") // Replace "/" with the correct root path if needed.

        // Query Firebase to find the item by restaurantName
        firebaseDatabase.orderByChild("restaurantName").equalTo(item.restaurantName).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        // Double-check mealType for an exact match (optional for stricter filtering)
                        val mealType = childSnapshot.child("mealType").value as? String
                        if (mealType == item.mealType) {
                            val crypticId = childSnapshot.key
                            crypticId?.let {
                                firebaseDatabase.child(it).removeValue()
                                    .addOnSuccessListener {
                                        Log.d("FirebaseDelete", "Deleted: ${item.restaurantName}")
                                        Toast.makeText(
                                            context,
                                            "${item.restaurantName} (${item.mealType}) deleted successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("FirebaseDeleteError", "Failed to delete ${item.restaurantName}: ${exception.message}")
                                    }
                            }
                        }
                    }
                } else {
                    Log.e("FirebaseDeleteError", "No matching data found for ${item.restaurantName}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseDeleteError", "Error querying data: ${exception.message}")
            }
    }
}