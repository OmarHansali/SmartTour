package com.smarttour.explore.api.model

data class TourMapResponse(
    val title: String,
    val badge: String,
    val progressLabel: String,
    val activeTourId: String,
    val nextStop: TourStopResponse?,
    val stops: List<TourStopResponse>,
    val routePoints: List<RoutePointResponse>
)

data class TourStopResponse(
    val poiId: String,
    val name: String,
    val distanceLabel: String,
    val walkTimeLabel: String,
    val isCurrent: Boolean
)

data class RoutePointResponse(
    val x: Float,
    val y: Float,
    val kind: String
)
