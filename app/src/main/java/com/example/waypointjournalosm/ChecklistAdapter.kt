package com.example.waypointjournalosm


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waypointjournalosm.databinding.ChecklistItemBinding

class ChecklistAdapter(
    private val restaurantItem: RestaurantItem
) : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val restaurantNameTextView: TextView =
            view.findViewById(R.id.restaurantNameChecklist)
        private val checkbox: CheckBox = view.findViewById(R.id.checkbox)

        // Bind data to views
        fun bind(restaurantName: String) {
            restaurantNameTextView.text = restaurantName
            checkbox.isChecked = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Shows the layout of the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checklist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Makes the RecyclerView only show limited CardViews
        //As the user scrolls up, old CardViews from above will be stored
        //The new CardViews will be shown from below
        restaurantItem.checklist.value?.get(position)?.let { restaurantName ->
            holder.bind(restaurantName)
        } // Bind restaurant name at position
    }

    override fun getItemCount(): Int = restaurantItem.checklist.value?.size ?: 0
        //To let the system know how many items needed to be displayed

    // Update the entire checklist when there's a change
    fun updateChecklist() {
        notifyDataSetChanged()  // Rebind the data on each change
    }

}