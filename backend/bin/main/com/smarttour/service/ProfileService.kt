package com.smarttour.service

import com.smarttour.dto.*
import com.smarttour.model.Favorite
import com.smarttour.model.VisitHistory
import com.smarttour.model.Place
import com.smarttour.repository.FavoriteRepository
import com.smarttour.repository.VisitHistoryRepository
import com.smarttour.repository.UserRepository
import com.smarttour.repository.PlaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProfileService(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val visitHistoryRepository: VisitHistoryRepository,
    private val placeRepository: PlaceRepository
) {

    fun getProfile(userId: UUID): ProfileResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        
        val totalVisits = visitHistoryRepository.countByUserId(userId)
        val totalFavorites = favoriteRepository.findByUserId(userId).size
        
        return ProfileResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            avatarUrl = user.avatarUrl,
            preferences = user.preferences,
            totalVisits = totalVisits,
            totalFavorites = totalFavorites
        )
    }
    
    fun getFavorites(userId: UUID): List<FavoriteResponse> {
        val favorites = favoriteRepository.findByUserId(userId)
        return favorites.map { favorite ->
            FavoriteResponse(
                id = favorite.id,
                place = favorite.place.toSummaryDto(),
                createdAt = favorite.createdAt
            )
        }
    }
    
    @Transactional
    fun addFavorite(request: FavoriteRequest): FavoriteResponse {
        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        val place = placeRepository.findById(request.placeId)
            .orElseThrow { IllegalArgumentException("Place not found") }
        
        if (favoriteRepository.existsByUserIdAndPlaceId(request.userId, request.placeId)) {
            throw IllegalStateException("Place is already in favorites")
        }
        
        val favorite = Favorite(user = user, place = place)
        val saved = favoriteRepository.save(favorite)
        
        return FavoriteResponse(
            id = saved.id,
            place = saved.place.toSummaryDto(),
            createdAt = saved.createdAt
        )
    }
    
    @Transactional
    fun removeFavorite(userId: UUID, placeId: UUID) {
        favoriteRepository.deleteByUserIdAndPlaceId(userId, placeId)
    }
    
    fun getVisitHistory(userId: UUID): List<VisitHistoryResponse> {
        val visits = visitHistoryRepository.findByUserIdOrderByVisitedAtDesc(userId)
        return visits.map { visit ->
            VisitHistoryResponse(
                id = visit.id,
                place = visit.place.toSummaryDto(),
                visitedAt = visit.visitedAt,
                userRating = visit.userRating,
                notes = visit.notes
            )
        }
    }
    
    @Transactional
    fun recordVisit(request: VisitRequest): VisitHistoryResponse {
        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        val place = placeRepository.findById(request.placeId)
            .orElseThrow { IllegalArgumentException("Place not found") }
        
        val visit = VisitHistory(
            user = user,
            place = place,
            userRating = request.rating,
            notes = request.notes
        )
        
        val saved = visitHistoryRepository.save(visit)
        
        return VisitHistoryResponse(
            id = saved.id,
            place = saved.place.toSummaryDto(),
            visitedAt = saved.visitedAt,
            userRating = saved.userRating,
            notes = saved.notes
        )
    }
}
