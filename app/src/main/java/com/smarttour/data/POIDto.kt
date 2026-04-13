package com.smarttour.data

data class POIDto(
    val id: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val imageUrl: String?
)