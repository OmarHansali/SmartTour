package com.smarttour.dto

import java.util.UUID

data class TripRequest(
    val userId: UUID,
    val name: String? = null,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Int = 5000,
    val maxPlaces: Int = 4,
    val categories: List<String>? = null
)

data class TripResponse(
    val id: UUID?,
    val name: String,
    val places: List<PlaceInTripDto>,
    val route: List<RoutePointDto>,
    val estimatedDurationMinutes: Int?,
    val estimatedDistanceMeters: Int?,
    val totalStops: Int
)

data class PlaceInTripDto(
    val id: UUID?,
    val name: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val category: String?,
    val rating: Double?,
    val imageUrl: String?,
    val address: String?,
    val order: Int,
    val estimatedVisitDurationMinutes: Int = 60
)

data class RoutePointDto(
    val latitude: Double,
    val longitude: Double,
    val order: Int,
    val placeName: String? = null
)
