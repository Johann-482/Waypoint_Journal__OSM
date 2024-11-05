package com.example.waypointjournalosm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : androidx.activity.ComponentActivity() {
    private val restaurantItem: RestaurantItem by viewModels()
    private lateinit var listAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_place)

        listAdapter = CategoryAdapter(
            onAddClick = { item ->
                Toast.makeText(this, "Add ${item.name} to List", Toast.LENGTH_SHORT).show()
            },
            onInfoClick = { item ->
                showInfoDialog(item)
            }
        )


        val recyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter

        restaurantItem.restaurantList.observe(this, Observer { restaurantList ->
            listAdapter.submitList(restaurantList)
        })
    }
    private fun showInfoDialog(item: RestaurantData) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Information about ${item.name}")
            .setCancelable(true)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Info")
        alert.show()
    }
}