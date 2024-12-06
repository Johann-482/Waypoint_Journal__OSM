package com.example.waypointjournalosm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.waypointjournalosm.databinding.ListItemBinding

class ListAdapter(
    private val onAddClick: (RestaurantData) -> Unit,
    private val onInfoClick: (RestaurantData) -> Unit
) : RecyclerView.Adapter<ListAdapter.RestaurantViewHolder>() {

    private var items: List<RestaurantData> = emptyList()
    private var filteredItems: List<RestaurantData> = emptyList()

    fun submitList(data: List<RestaurantData>) {
        items = data
        filteredItems = data  // Initially, show all items
        notifyDataSetChanged()
    }

    // Filter the list based on the query
    fun filterList(query: String) {
        filteredItems = if (query.isEmpty()) {
            items // Show all items if query is empty
        } else {
            items.filter { restaurant ->
                restaurant.rMenu.contains(query, ignoreCase = true)
            }
        }

        // Notify RecyclerView to update with the filtered items
        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantItem: RestaurantData) {
            binding.restaurantNameList.text = restaurantItem.name
            binding.restaurantMenuList.text = restaurantItem.rMenu
            binding.addToListButton.setOnClickListener { onAddClick(restaurantItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurantItem = filteredItems[position] // Bind from filteredItems only
        holder.bind(restaurantItem)
    }

    override fun getItemCount(): Int {
        return filteredItems.size // Show the size of the filtered list
    }
}
