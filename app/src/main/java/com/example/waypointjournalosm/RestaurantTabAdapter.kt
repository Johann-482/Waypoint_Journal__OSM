package com.example.waypointjournalosm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class RestaurantTabAdapter(
    private val restaurantNames: List<String>,
    private val onRestaurantSelected: (String) -> Unit
) : RecyclerView.Adapter<RestaurantTabAdapter.RestaurantTabViewHolder>() {

    class RestaurantTabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.tvTabRestaurantName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantTabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_restaurant_tab, parent, false
        )
        return RestaurantTabViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantTabViewHolder, position: Int) {
        val restaurantName = restaurantNames[position]
        holder.button.text = restaurantName

        // Handle button click
        holder.button.setOnClickListener {
            onRestaurantSelected(restaurantName)
        }
    }

    override fun getItemCount() = restaurantNames.size
}