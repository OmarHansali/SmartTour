package com.smarttour.explore.api.model

data class ExploreFeedResponse(
    val city: String,
    val greeting: String,
    val title: String,
    val searchPlaceholder: String,
    val nearbyHighlights: List<ExplorePoiResponse>,
    val featuredTours: List<FeaturedTourResponse>
)

data class ExplorePoiResponse(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val distanceMeters: Int,
    val rating: Double,
    val imageUrl: String?,
    val latitude: Double,
    val longitude: Double
)

data class FeaturedTourResponse(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val stopCount: Int,
    val accentColorHex: String
)
