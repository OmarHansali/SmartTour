package com.smarttour.domain

data class ExploreFeed(
    val city: String,
    val greeting: String,
    val title: String,
    val searchPlaceholder: String,
    val nearbyHighlights: List<ExplorePoi>,
    val featuredTours: List<FeaturedTour>
)

data class ExplorePoi(
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

data class FeaturedTour(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val stopCount: Int,
    val accentColorHex: String
)

data class PoiDetail(
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
    val actions: PoiActions
)

data class PoiActions(
    val canAddToTour: Boolean,
    val canNavigate: Boolean,
    val canPlayNarration: Boolean
)

data class TourMap(
    val title: String,
    val badge: String,
    val progressLabel: String,
    val activeTourId: String,
    val nextStop: TourStop,
    val stops: List<TourStop>,
    val routePoints: List<RoutePoint>
)

data class TourStop(
    val poiId: String,
    val name: String,
    val distanceLabel: String,
    val walkTimeLabel: String,
    val isCurrent: Boolean
)

data class RoutePoint(
    val x: Float,
    val y: Float,
    val kind: String
)

data class ArCamera(
    val focusedPoiId: String,
    val focusedPoiName: String,
    val focusedPoiSubtitle: String,
    val actionHint: String,
    val metrics: List<ArMetric>,
    val overlays: List<ArOverlay>,
    val nowPlaying: NowPlaying
)

data class ArMetric(
    val label: String,
    val value: String
)

data class ArOverlay(
    val poiId: String,
    val name: String,
    val subtitle: String,
    val distanceMeters: Int,
    val rating: Double,
    val eraLabel: String
)

data class NowPlaying(
    val title: String,
    val subtitle: String,
    val isPlaying: Boolean
)

data class Profile(
    val fullName: String,
    val initials: String,
    val levelLabel: String,
    val stats: List<ProfileStat>,
    val settings: List<ProfileSetting>
)

data class ProfileStat(
    val value: String,
    val label: String
)

data class ProfileSetting(
    val label: String,
    val value: String
)
