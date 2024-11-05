package com.example.waypointjournalosm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.waypointjournalosm.databinding.ListItemBinding

class CategoryAdapter(
    private val onAddClick: (RestaurantData) -> Unit,
    private val onInfoClick: (RestaurantData) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.RestaurantViewHolder>() {

    private var items: List<RestaurantData> = emptyList()

    fun submitList(data: List<RestaurantData>) {
        items = data
        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantItem: RestaurantData) {
            binding.restaurantNameList.text = restaurantItem.name
            binding.restaurantMenuList.text = restaurantItem.rMenu
            binding.addToListButton.setOnClickListener { onAddClick(restaurantItem) }
            binding.infoButton.setOnClickListener { onInfoClick(restaurantItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}