package com.example.waypointjournalosm


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChecklistAdapter : RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    private val restaurantList = mutableListOf<String>()

    fun addRestaurantToChecklist(restaurantName: String) {
        if (!restaurantList.contains(restaurantName)) {
            restaurantList.add(restaurantName)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        //Shows the layout of the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checklist_item, parent, false)
        return ChecklistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        //Makes the RecyclerView only show limited CardViews
        //As the user scrolls up, old CardViews from above will be stored
        //The new CardViews will be shown from below
        holder.bind(restaurantList[position])
    }

    override fun getItemCount(): Int = restaurantList.size
        //To let the system know how many items needed to be displayed

    class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //grabbing the views created from the layout file
        private val restaurantNameTextView: TextView = itemView.findViewById(R.id.restaurantName)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(restaurantName: String) {
            restaurantNameTextView.text = restaurantName
        }
    }
}