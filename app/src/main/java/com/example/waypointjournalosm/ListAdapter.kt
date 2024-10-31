package com.example.waypointjournalosm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private var categoryList: List<RestaurantItem> = emptyList(),
    private val onAddClick: (RestaurantItem) -> Unit,
    private val onInfoClick: (RestaurantItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantNameList: TextView = view.findViewById(R.id.restaurantNameList)
        val restaurantMenuList: TextView = view.findViewById(R.id.restaurantMenuList) // Second TextView
        val addToListButton: Button = view.findViewById(R.id.addToListButton)
        val infoButton: ImageView = view.findViewById(R.id.infoButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurantItem = categoryList[position]
        holder.restaurantNameList.text = restaurantItem.name
        holder.restaurantMenuList.text = restaurantItem.rMenu // Bind description text

        // Set click listeners for each button
        holder.addToListButton.setOnClickListener {
            onAddClick(restaurantItem)
        }

        holder.infoButton.setOnClickListener {
            onInfoClick(restaurantItem)
        }
    }

    override fun getItemCount(): Int = categoryList.size
}