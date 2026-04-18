package com.smarttour.data

data class TourRouteDto(
    val id: String,
    val tourName: String,
    val totalDistanceKm: Double,
    val estimatedDurationMin: Int,
    val stops: List<TourStopDto>,
    val currentStopIndex: Int
)
