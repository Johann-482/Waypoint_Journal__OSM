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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checklist_item, parent, false)
        return ChecklistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        holder.bind(restaurantList[position])
    }

    override fun getItemCount(): Int = restaurantList.size

    class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val restaurantNameTextView: TextView = itemView.findViewById(R.id.restaurantName)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(restaurantName: String) {
            restaurantNameTextView.text = restaurantName
        }
    }
}