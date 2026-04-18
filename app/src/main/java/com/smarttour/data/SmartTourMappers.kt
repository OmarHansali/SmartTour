package com.smarttour.data

import com.smarttour.domain.ArCamera
import com.smarttour.domain.ArMetric
import com.smarttour.domain.ArOverlay
import com.smarttour.domain.ExploreFeed
import com.smarttour.domain.ExplorePoi
import com.smarttour.domain.FeaturedTour
import com.smarttour.domain.NowPlaying
import com.smarttour.domain.PoiActions
import com.smarttour.domain.PoiDetail
import com.smarttour.domain.Profile
import com.smarttour.domain.ProfileSetting
import com.smarttour.domain.ProfileStat
import com.smarttour.domain.RoutePoint
import com.smarttour.domain.TourMap
import com.smarttour.domain.TourStop

fun ExploreFeedDto.toDomain(): ExploreFeed = ExploreFeed(
    city = city,
    greeting = greeting,
    title = title,
    searchPlaceholder = searchPlaceholder,
    nearbyHighlights = nearbyHighlights.map { it.toDomain() },
    featuredTours = featuredTours.map { it.toDomain() }
)

fun ExplorePoiDto.toDomain(): ExplorePoi = ExplorePoi(
    id = id,
    name = name,
    description = description,
    category = category,
    distanceMeters = distanceMeters,
    rating = rating,
    imageUrl = imageUrl,
    latitude = latitude,
    longitude = longitude
)

fun FeaturedTourDto.toDomain(): FeaturedTour = FeaturedTour(
    id = id,
    title = title,
    description = description,
    durationMinutes = durationMinutes,
    stopCount = stopCount,
    accentColorHex = accentColorHex
)

fun PoiDetailDto.toDomain(): PoiDetail = PoiDetail(
    id = id,
    name = name,
    subtitle = subtitle,
    status = status,
    ratingLabel = ratingLabel,
    distanceLabel = distanceLabel,
    admissionLabel = admissionLabel,
    eraLabel = eraLabel,
    description = description,
    narrationTitle = narrationTitle,
    narrationSubtitle = narrationSubtitle,
    imageUrl = imageUrl,
    actions = actions.toDomain()
)

fun PoiActionsDto.toDomain(): PoiActions = PoiActions(
    canAddToTour = canAddToTour,
    canNavigate = canNavigate,
    canPlayNarration = canPlayNarration
)

fun TourMapDto.toDomain(): TourMap = TourMap(
    title = title,
    badge = badge,
    progressLabel = progressLabel,
    activeTourId = activeTourId,
    nextStop = nextStop.toDomain(),
    stops = stops.map { it.toDomain() },
    routePoints = routePoints.map { it.toDomain() }
)

fun TourStopDto.toDomain(): TourStop = TourStop(
    poiId = poiId,
    name = name,
    distanceLabel = distanceLabel,
    walkTimeLabel = walkTimeLabel,
    isCurrent = isCurrent
)

fun RoutePointDto.toDomain(): RoutePoint = RoutePoint(
    x = x,
    y = y,
    kind = kind
)

fun ArCameraDto.toDomain(): ArCamera = ArCamera(
    focusedPoiId = focusedPoiId,
    focusedPoiName = focusedPoiName,
    focusedPoiSubtitle = focusedPoiSubtitle,
    actionHint = actionHint,
    metrics = metrics.map { it.toDomain() },
    overlays = overlays.map { it.toDomain() },
    nowPlaying = nowPlaying.toDomain()
)

fun ArMetricDto.toDomain(): ArMetric = ArMetric(label = label, value = value)

fun ArOverlayDto.toDomain(): ArOverlay = ArOverlay(
    poiId = poiId,
    name = name,
    subtitle = subtitle,
    distanceMeters = distanceMeters,
    rating = rating,
    eraLabel = eraLabel
)

fun NowPlayingDto.toDomain(): NowPlaying = NowPlaying(
    title = title,
    subtitle = subtitle,
    isPlaying = isPlaying
)

fun ProfileDto.toDomain(): Profile = Profile(
    fullName = fullName,
    initials = initials,
    levelLabel = levelLabel,
    stats = stats.map { it.toDomain() },
    settings = settings.map { it.toDomain() }
)

fun ProfileStatDto.toDomain(): ProfileStat = ProfileStat(value = value, label = label)

fun ProfileSettingDto.toDomain(): ProfileSetting = ProfileSetting(label = label, value = value)
