package com.example.waypointjournalosm

data class Restaurant(
    val name: String,
    var isChecked: Boolean = false // Default to not selected
)