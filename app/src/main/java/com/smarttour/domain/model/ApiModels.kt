package com.smarttour.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExploreFeedResponse(
    val city: String,
    val greeting: String,
    val title: String,
    val searchPlaceholder: String,
    val nearbyHighlights: List<ExplorePoiResponse>,
    val featuredTours: List<FeaturedTourResponse>
)

@JsonClass(generateAdapter = true)
data class ExplorePoiResponse(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val distanceMeters: Double,
    val rating: Double,
    val imageUrl: String? = null,
    val latitude: Double,
    val longitude: Double
)

@JsonClass(generateAdapter = true)
data class FeaturedTourResponse(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val stopCount: Int,
    val accentColorHex: String
)

@JsonClass(generateAdapter = true)
data class PoiDetailResponse(
    val id: String,
    val name: String,
    val subtitle: String,
    val status: String,
    val ratingLabel: String,
    val distanceLabel: String,
    val admissionLabel: String,
    val eraLabel: String,
    val description: String,
    val narrationTitle: String,
    val narrationSubtitle: String,
    val imageUrl: String? = null,
    val actions: PoiActionsResponse
)

@JsonClass(generateAdapter = true)
data class PoiActionsResponse(
    val canAddToTour: Boolean,
    val canNavigate: Boolean,
    val canPlayNarration: Boolean
)

@JsonClass(generateAdapter = true)
data class TourMapResponse(
    val title: String,
    val badge: String,
    val progressLabel: String,
    val activeTourId: String,
    val nextStop: TourStopResponse? = null,
    val stops: List<TourStopResponse>,
    val routePoints: List<RoutePointResponse>
)

@JsonClass(generateAdapter = true)
data class TourStopResponse(
    val poiId: String,
    val name: String,
    val distanceLabel: String,
    val walkTimeLabel: String,
    val isCurrent: Boolean
)

@JsonClass(generateAdapter = true)
data class RoutePointResponse(
    val x: Float,
    val y: Float,
    val type: String
)

@JsonClass(generateAdapter = true)
data class ArCameraResponse(
    val focusedPoiId: String,
    val focusedPoiName: String,
    val focusedPoiSubtitle: String,
    val actionHint: String,
    val metrics: List<ArMetricResponse>,
    val overlays: List<ArOverlayResponse>,
    val nowPlaying: NowPlayingResponse
)

@JsonClass(generateAdapter = true)
data class ArMetricResponse(
    val label: String,
    val value: String
)

@JsonClass(generateAdapter = true)
data class ArOverlayResponse(
    val poiId: String,
    val name: String,
    val subtitle: String,
    val distanceMeters: Double,
    val rating: Double,
    val eraLabel: String
)

@JsonClass(generateAdapter = true)
data class NowPlayingResponse(
    val title: String,
    val subtitle: String,
    val isPlaying: Boolean
)

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    val fullName: String,
    val initials: String,
    val levelLabel: String,
    val stats: List<ProfileStatResponse>,
    val settings: List<ProfileSettingResponse>
)

@JsonClass(generateAdapter = true)
data class ProfileStatResponse(
    val value: String,
    val label: String
)

@JsonClass(generateAdapter = true)
data class ProfileSettingResponse(
    val label: String,
    val value: String
)
