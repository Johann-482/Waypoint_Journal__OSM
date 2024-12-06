package com.example.waypointjournalosm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(private val imageUrls: MutableList<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    // ViewHolder setup
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    // Update the list of image URLs
    fun updateImages(newImageUrls: List<String>) {
        imageUrls.clear()  // Optional: Clear previous images if necessary
        imageUrls.addAll(newImageUrls)  // Add new image URLs
        notifyDataSetChanged()  // Notify the adapter to refresh the RecyclerView
    }

    // Clear all images (e.g., when an empty folder is selected)
    fun clearImages() {
        imageUrls.clear() // Clear the current list
        notifyDataSetChanged() // Notify RecyclerView to refresh
    }

    // Add new images to the existing list (for lazy loading)
    fun addImages(newImageUrls: List<String>) {
        imageUrls.addAll(newImageUrls)  // Add new images to the list
        notifyDataSetChanged()  // Refresh RecyclerView
    }
}