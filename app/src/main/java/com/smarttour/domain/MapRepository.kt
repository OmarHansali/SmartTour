package com.smarttour.domain

interface MapRepository {
    suspend fun getActiveTourRoute(tourId: String): TourRoute
    suspend fun markStopVisited(tourId: String, stopId: String)
}
