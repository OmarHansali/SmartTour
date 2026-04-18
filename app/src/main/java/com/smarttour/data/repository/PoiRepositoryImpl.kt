package com.smarttour.data.repository

import com.smarttour.data.local.SmartTourDatabase
import com.smarttour.data.remote.SmartTourApiService
import com.smarttour.domain.model.PoiDetailResponse
import com.smarttour.domain.repository.PoiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PoiRepositoryImpl(
    private val apiService: SmartTourApiService,
    private val database: SmartTourDatabase
) : PoiRepository {

    override suspend fun getExploreFeed(): Flow<List<PoiDetailResponse>> {
        return try {
            val response = apiService.getExploreFeed()
            flowOf(response.nearbyHighlights.map { 
                PoiDetailResponse(
                    id = it.id,
                    name = it.name,
                    subtitle = it.category,
                    status = "Open",
                    ratingLabel = "${it.rating} ★",
                    distanceLabel = "${it.distanceMeters.toInt()} m",
                    admissionLabel = "Check on site",
                    eraLabel = "Historic",
                    description = it.description,
                    narrationTitle = "AI narration ready",
                    narrationSubtitle = "Listen to facts",
                    imageUrl = it.imageUrl,
                    actions = com.smarttour.domain.model.PoiActionsResponse(
                        canAddToTour = true,
                        canNavigate = true,
                        canPlayNarration = true
                    )
                )
            })
        } catch (e: Exception) {
            flowOf(emptyList())
        }
    }

    override suspend fun getPoiDetail(poiId: String): PoiDetailResponse {
        return apiService.getPoiDetail(poiId)
    }

    override suspend fun getNearbyPois(
        latitude: Double,
        longitude: Double,
        radiusMeters: Double
    ): Flow<List<PoiDetailResponse>> {
        return flowOf(emptyList())
    }

    override suspend fun searchPois(query: String): Flow<List<PoiDetailResponse>> {
        return flowOf(emptyList())
    }
}
