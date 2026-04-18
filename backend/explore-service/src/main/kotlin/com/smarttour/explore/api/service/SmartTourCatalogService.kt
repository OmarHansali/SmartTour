package com.smarttour.explore.api.service

import com.smarttour.explore.api.model.*
import com.smarttour.explore.domain.SeedCatalog
import com.smarttour.explore.domain.repository.PointOfInterestRepository
import com.smarttour.explore.domain.repository.TourRepository
import com.smarttour.explore.domain.repository.UserRepository
import com.smarttour.explore.domain.repository.NarrationRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Coordinate

@Service
class SmartTourCatalogService(
    private val seedCatalog: SeedCatalog,
    private val poiRepository: PointOfInterestRepository,
    private val tourRepository: TourRepository,
    private val userRepository: UserRepository,
    private val narrationRepository: NarrationRepository
) {

    fun getExploreFeed(): ExploreFeedResponse {
        val publishedTours = tourRepository.findByIsPublishedTrue()
        val topRatedPois = poiRepository.findTop10ByOrderByRatingDesc()
        
        return ExploreFeedResponse(
            city = "Marrakech",
            greeting = "Good morning",
            title = "Explore SmartTour",
            searchPlaceholder = "Search places, monuments...",
            nearbyHighlights = topRatedPois.map { poi ->
                ExplorePoiResponse(
                    id = poi.id,
                    name = poi.name,
                    description = poi.shortDescription,
                    category = poi.category,
                    distanceMeters = poi.distanceMeters.toInt(),
                    rating = poi.rating,
                    imageUrl = poi.imageUrl,
                    latitude = poi.location?.x ?: 0.0,
                    longitude = poi.location?.y ?: 0.0
                )
            },
            featuredTours = publishedTours.take(5).map { tour ->
                FeaturedTourResponse(
                    id = tour.id,
                    title = tour.title,
                    description = tour.description,
                    durationMinutes = tour.durationMinutes,
                    stopCount = tour.pointsOfInterest.size,
                    accentColorHex = "#FF6B35"
                )
            }
        )
    }

    fun getPoiDetail(poiId: String): PoiDetailResponse {
        val poi = poiRepository.findById(poiId).orElseThrow {
            IllegalArgumentException("POI not found")
        }
        
        return PoiDetailResponse(
            id = poi.id,
            name = poi.name,
            subtitle = poi.category,
            status = if (poi.location != null) "Open" else "Unknown",
            ratingLabel = "${poi.rating} ★",
            distanceLabel = "${poi.distanceMeters.toInt()} m",
            admissionLabel = poi.entryFee,
            eraLabel = "Historic",
            description = poi.description,
            narrationTitle = "AI narration ready",
            narrationSubtitle = "Listen to facts in your language",
            imageUrl = poi.imageUrl,
            actions = PoiActionsResponse(
                canAddToTour = true,
                canNavigate = true,
                canPlayNarration = true
            )
        )
    }

    fun getTourMap(): TourMapResponse {
        val tours = tourRepository.findByIsPublishedTrue()
        val activeTour = tours.firstOrNull() ?: return TourMapResponse(
            title = "Tour map",
            badge = "No active tour",
            progressLabel = "0/0",
            activeTourId = "",
            nextStop = null,
            stops = emptyList(),
            routePoints = emptyList()
        )
        
        val stops = activeTour.pointsOfInterest.mapIndexed { index, poi ->
            TourStopResponse(
                poiId = poi.id,
                name = poi.name,
                distanceLabel = "${poi.distanceMeters.toInt()} m",
                walkTimeLabel = "~${4 + index * 2} min walk",
                isCurrent = index == 0
            )
        }

        return TourMapResponse(
            title = "Tour map",
            badge = "Active tour",
            progressLabel = "${1}/${stops.size}",
            activeTourId = activeTour.id,
            nextStop = stops.firstOrNull(),
            stops = stops,
            routePoints = listOf(
                RoutePointResponse(0.10f, 0.80f, "start"),
                RoutePointResponse(0.35f, 0.38f, "waypoint"),
                RoutePointResponse(0.60f, 0.48f, "waypoint"),
                RoutePointResponse(0.90f, 0.20f, "destination")
            )
        )
    }

    fun getArCamera(focusedPoiId: String?): ArCameraResponse {
        val poiId = focusedPoiId ?: poiRepository.findAll().firstOrNull()?.id
        
        if (poiId == null) {
            return ArCameraResponse(
                focusedPoiId = "",
                focusedPoiName = "No POIs available",
                focusedPoiSubtitle = "",
                actionHint = "Add POIs to the database",
                metrics = emptyList(),
                overlays = emptyList(),
                nowPlaying = NowPlayingResponse(
                    title = "Nothing playing",
                    subtitle = "",
                    isPlaying = false
                )
            )
        }
        
        val poi = poiRepository.findById(poiId).orElseThrow {
            IllegalArgumentException("POI not found")
        }
        
        val allPois = poiRepository.findAll().take(10)
        
        return ArCameraResponse(
            focusedPoiId = poi.id,
            focusedPoiName = poi.name,
            focusedPoiSubtitle = poi.category,
            actionHint = poi.description,
            metrics = listOf(
                ArMetricResponse("Distance", "${poi.distanceMeters.toInt()} m"),
                ArMetricResponse("Rating", "${poi.rating} ★"),
                ArMetricResponse("Category", poi.category)
            ),
            overlays = allPois.map {
                ArOverlayResponse(
                    poiId = it.id,
                    name = it.name,
                    subtitle = it.category,
                    distanceMeters = it.distanceMeters.toInt(),
                    rating = it.rating,
                    eraLabel = "Historic"
                )
            },
            nowPlaying = NowPlayingResponse(
                title = poi.name,
                subtitle = "Now playing narration",
                isPlaying = true
            )
        )
    }

    fun getProfile(): ProfileResponse {
        return ProfileResponse(
            fullName = "Traveler",
            initials = "T",
            levelLabel = "Explorer Level 5",
            stats = listOf(
                ProfileStatResponse("42", "Tours Completed"),
                ProfileStatResponse("156", "POIs Visited"),
                ProfileStatResponse("€2,450", "Saved")
            ),
            settings = listOf(
                ProfileSettingResponse("Notifications", "Enabled"),
                ProfileSettingResponse("Offline Maps", "Downloaded"),
                ProfileSettingResponse("Language", "English")
            )
        )
    }
}