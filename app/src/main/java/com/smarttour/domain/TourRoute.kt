package com.smarttour.domain

data class TourRoute(
    val id: String,
    val tourName: String,
    val totalDistanceKm: Double,
    val estimatedDurationMin: Int,
    val stops: List<TourStop>,
    val currentStopIndex: Int
) {
    val currentStop: TourStop?
        get() = stops.getOrNull(currentStopIndex)

    val nextStop: TourStop?
        get() = stops.getOrNull(currentStopIndex + 1)

    val progressFraction: Float
        get() = if (stops.isEmpty()) 0f else stops.count { it.isVisited }.toFloat() / stops.size
}
