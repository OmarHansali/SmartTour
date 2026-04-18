package com.smarttour.data

import com.smarttour.domain.MapRepository
import com.smarttour.domain.TourRoute
import com.smarttour.network.MapApiService

class MapRepositoryImpl(
    private val apiService: MapApiService
) : MapRepository {

    override suspend fun getActiveTourRoute(tourId: String): TourRoute {
        return apiService.getActiveTourRoute(tourId).toDomain()
    }

    override suspend fun markStopVisited(tourId: String, stopId: String) {
        apiService.markStopVisited(tourId, stopId)
    }
}
