package com.smarttour.explore.api.model

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
    val imageUrl: String?,
    val actions: PoiActionsResponse
)

data class PoiActionsResponse(
    val canAddToTour: Boolean,
    val canNavigate: Boolean,
    val canPlayNarration: Boolean
)
