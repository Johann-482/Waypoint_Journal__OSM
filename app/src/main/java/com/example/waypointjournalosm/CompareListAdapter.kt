package com.example.waypointjournalosm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.waypointjournalosm.databinding.CompareListItemBinding

class CompareListAdapter(
    private val onAddClick: (RestaurantData) -> Unit,
    private val onInfoClick: (RestaurantData) -> Unit
) : RecyclerView.Adapter<CompareListAdapter.RestaurantViewHolder>() {

    private var items: List<RestaurantData> = emptyList()

    fun submitList(data: List<RestaurantData>) {
        items = data
        notifyDataSetChanged()
    }
    inner class RestaurantViewHolder(private val binding: CompareListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantItem: RestaurantData) {
            binding.compareRestaurantNameList.text = restaurantItem.name
            binding.compareRestaurantMenuList.text = restaurantItem.rMenu.joinToString(", ")
            binding.addToComparisonButton.setOnClickListener { onAddClick(restaurantItem) }
            binding.comparisonInfoButton.setOnClickListener { onInfoClick(restaurantItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = CompareListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}