package com.smarttour.dto

import java.util.UUID
import java.time.LocalDateTime

data class ProfileResponse(
    val id: UUID?,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val preferences: Set<String>,
    val totalVisits: Long,
    val totalFavorites: Int
)

data class FavoriteRequest(
    val userId: UUID,
    val placeId: UUID
)

data class FavoriteResponse(
    val id: UUID?,
    val place: PlaceSummaryDto,
    val createdAt: LocalDateTime
)

data class PlaceSummaryDto(
    val id: UUID?,
    val name: String,
    val category: String?,
    val imageUrl: String?,
    val address: String?,
    val rating: Double?
)

data class VisitHistoryResponse(
    val id: UUID?,
    val place: PlaceSummaryDto,
    val visitedAt: LocalDateTime,
    val userRating: Double?,
    val notes: String?
)

data class VisitRequest(
    val userId: UUID,
    val placeId: UUID,
    val rating: Double? = null,
    val notes: String? = null
)
