package com.smarttour.data
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExploreFeedDto(
    val city: String,
    val greeting: String,
    val title: String,
    val searchPlaceholder: String,
    val nearbyHighlights: List<ExplorePoiDto>,
    val featuredTours: List<FeaturedTourDto>
)

@JsonClass(generateAdapter = true)
data class ExplorePoiDto(
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

@JsonClass(generateAdapter = true)
data class FeaturedTourDto(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val stopCount: Int,
    val accentColorHex: String
)

@JsonClass(generateAdapter = true)
data class PoiDetailDto(
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
    val imageUrl: String?,
    val actions: PoiActionsDto
)

@JsonClass(generateAdapter = true)
data class PoiActionsDto(
    val canAddToTour: Boolean,
    val canNavigate: Boolean,
    val canPlayNarration: Boolean
)

@JsonClass(generateAdapter = true)
data class TourMapDto(
    val title: String,
    val badge: String,
    val progressLabel: String,
    val activeTourId: String,
    val nextStop: TourStopDto,
    val stops: List<TourStopDto>,
    val routePoints: List<RoutePointDto>
)

@JsonClass(generateAdapter = true)
data class TourStopDto(
    val poiId: String,
    val name: String,
    val distanceLabel: String,
    val walkTimeLabel: String,
    val isCurrent: Boolean
)

@JsonClass(generateAdapter = true)
data class RoutePointDto(
    val x: Float,
    val y: Float,
    val kind: String
)

@JsonClass(generateAdapter = true)
data class ArCameraDto(
    val focusedPoiId: String,
    val focusedPoiName: String,
    val focusedPoiSubtitle: String,
    val actionHint: String,
    val metrics: List<ArMetricDto>,
    val overlays: List<ArOverlayDto>,
    val nowPlaying: NowPlayingDto
)

@JsonClass(generateAdapter = true)
data class ArMetricDto(
    val label: String,
    val value: String
)

@JsonClass(generateAdapter = true)
data class ArOverlayDto(
    val poiId: String,
    val name: String,
    val subtitle: String,
    val distanceMeters: Int,
    val rating: Double,
    val eraLabel: String
)

@JsonClass(generateAdapter = true)
data class NowPlayingDto(
    val title: String,
    val subtitle: String,
    val isPlaying: Boolean
)

@JsonClass(generateAdapter = true)
data class ProfileDto(
    val fullName: String,
    val initials: String,
    val levelLabel: String,
    val stats: List<ProfileStatDto>,
    val settings: List<ProfileSettingDto>
)

@JsonClass(generateAdapter = true)
data class ProfileStatDto(
    val value: String,
    val label: String
)

@JsonClass(generateAdapter = true)
data class ProfileSettingDto(
    val label: String,
    val value: String
)
