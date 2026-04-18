package com.smarttour.data

import com.smarttour.domain.TourRoute
import com.smarttour.domain.TourStop

fun TourStopDto.toDomain(): TourStop = TourStop(
    id = id,
    name = name,
    description = description,
    latitude = latitude,
    longitude = longitude,
    order = order,
    isVisited = isVisited,
    imageUrl = imageUrl
)

fun TourRouteDto.toDomain(): TourRoute = TourRoute(
    id = id,
    tourName = tourName,
    totalDistanceKm = totalDistanceKm,
    estimatedDurationMin = estimatedDurationMin,
    stops = stops.map { it.toDomain() },
    currentStopIndex = currentStopIndex
)
