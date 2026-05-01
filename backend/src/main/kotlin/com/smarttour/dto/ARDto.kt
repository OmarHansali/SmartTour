package com.smarttour.dto

data class ARAnalysisRequest(
    val latitude: Double,
    val longitude: Double,
    val imageBase64: String? = null,
    val detectedObjects: List<String>? = null,
    val compassHeading: Double? = null
)

data class ARAnalysisResponse(
    val detectedObjects: List<DetectedObjectDto>,
    val nearestPlace: NearestPlaceDto?,
    val direction: DirectionDto?,
    val description: String
)

data class DetectedObjectDto(
    val name: String,
    val confidence: Double,
    val description: String
)

data class NearestPlaceDto(
    val name: String,
    val distanceMeters: Double,
    val bearing: Double,
    val category: String?,
    val description: String?
)

data class DirectionDto(
    val heading: Double,
    val instruction: String,
    val arrowRotation: Double
)
