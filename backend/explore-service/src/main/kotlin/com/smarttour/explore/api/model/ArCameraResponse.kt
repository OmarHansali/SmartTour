package com.smarttour.explore.api.model

data class ArCameraResponse(
    val focusedPoiId: String,
    val focusedPoiName: String,
    val focusedPoiSubtitle: String,
    val actionHint: String,
    val metrics: List<ArMetricResponse>,
    val overlays: List<ArOverlayResponse>,
    val nowPlaying: NowPlayingResponse
)

data class ArMetricResponse(
    val label: String,
    val value: String
)

data class ArOverlayResponse(
    val poiId: String,
    val name: String,
    val subtitle: String,
    val distanceMeters: Int,
    val rating: Double,
    val eraLabel: String
)

data class NowPlayingResponse(
    val title: String,
    val subtitle: String,
    val isPlaying: Boolean
)
