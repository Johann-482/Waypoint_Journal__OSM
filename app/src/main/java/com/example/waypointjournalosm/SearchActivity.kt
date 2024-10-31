package com.example.waypointjournalosm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_place)

        val categoryList = listOf(
            RestaurantItem("Food and Drink", "Find the best places to eat and drink."),
            RestaurantItem("Lodging", "Explore lodging options for every budget."),
            RestaurantItem("Shopping", "Discover shopping spots in the area."),
        )

        val recyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CategoryAdapter(categoryList,
            onAddClick = { item ->
                Toast.makeText(this, "Add ${item.name} to List", Toast.LENGTH_SHORT).show()
            },
            onInfoClick = { item ->
                showInfoDialog(item)
            }
        )


    }
    private fun showInfoDialog(item: RestaurantItem) {
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