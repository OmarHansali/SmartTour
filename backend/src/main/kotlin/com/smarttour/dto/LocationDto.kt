package com.smarttour.dto

import java.util.UUID

data class LocationRequest(
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Int = 5000
)

data class NearbyPlaceDto(
    val id: UUID?,
    val name: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val category: String?,
    val rating: Double?,
    val imageUrl: String?,
    val address: String?,
    val distanceMeters: Double?
)

data class NearbyPlacesResponse(
    val places: List<NearbyPlaceDto>,
    val userLatitude: Double,
    val userLongitude: Double,
    val totalFound: Int
)
