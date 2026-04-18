package com.smarttour.data

data class TourStopDto(
    val id: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val order: Int,
    val isVisited: Boolean,
    val imageUrl: String?
)
