package com.example.waypointjournalosm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.waypointjournalosm.databinding.CompareListItemBinding

class CompareListAdapter(
    private val onAddClick: (RestaurantData) -> Unit,
    private val onInfoClick: (RestaurantData) -> Unit,
    private val onItemClick: (RestaurantData) -> Unit
) : RecyclerView.Adapter<CompareListAdapter.RestaurantViewHolder>() {

    private var items: List<RestaurantData> = emptyList()
    private val selectedPositions = mutableListOf<Int>() // Track selected positions for radio buttons
    private var filteredItems: List<RestaurantData> = emptyList()
    private val selectedRestaurants = mutableListOf<RestaurantItem>()

    fun toggleSelection(item: RestaurantItem) {
        if (selectedRestaurants.contains(item)) {
            selectedRestaurants.remove(item)
        } else if (selectedRestaurants.size < 2) {
            selectedRestaurants.add(item)
        }
        notifyDataSetChanged()
    }


    fun submitList(data: List<RestaurantData>) {
        items = data
        filteredItems = data  // Initially, show all items
        notifyDataSetChanged()
    }

    // Filter the list based on the query
    fun filterList(query: String) {
        // Filter the items by name or menu if query is not empty
        filteredItems = if (query.isEmpty()) {
            items // Show all items if query is empty
        } else {
            items.filter { restaurant ->
                restaurant.rMenu.contains(query, ignoreCase = true) || restaurant.name.contains(query, ignoreCase = true)
            }
        }

        // Notify RecyclerView to update with the filtered items
        notifyDataSetChanged()  // This ensures the RecyclerView is notified every time the list is filtered
    }

    fun getSelectedRestaurants(): List<RestaurantData> {
        return items.filterIndexed { index, _ -> selectedPositions.contains(index) }
    }

    inner class RestaurantViewHolder(private val binding: CompareListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantItem: RestaurantData) {
            // Set restaurant name and menu
            binding.compareRestaurantNameList.text = restaurantItem.name
            binding.compareRestaurantMenuList.text = restaurantItem.rMenu

            // Set the rating on the RatingBar
            binding.ratingBar.rating = restaurantItem.restaurantRating.toFloat() // Assuming restaurantRating is an Int


            // Manage radio button selection
            binding.radioButton.isChecked = selectedPositions.contains(position)

            binding.radioButton.setOnClickListener {
                if (binding.radioButton.isChecked) {
                    if (selectedPositions.size < 2) {
                        selectedPositions.add(position)
                    } else {
                        // Deselect the first selected item if selecting a third
                        val firstSelectedPosition = selectedPositions.removeAt(0)
                        notifyItemChanged(firstSelectedPosition)
                        selectedPositions.add(position)
                    }
                } else {
                    // Remove from selected if unchecked
                    selectedPositions.remove(position)
                }

                // Notify activity about the selected item
                onItemClick(restaurantItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = CompareListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(items[position])
        if (filteredItems.isNotEmpty()) {
            holder.bind(filteredItems[position])
        }
    }

    override fun getItemCount(): Int {
        return filteredItems.size // Ensure you return the size of filteredItems, not items
        return items.size
    }
}