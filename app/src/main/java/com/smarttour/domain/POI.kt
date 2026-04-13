package com.smarttour.domain

data class POI(
    val id: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val imageUrl: String?
)