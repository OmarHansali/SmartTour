package com.smarttour.controller

import com.smarttour.dto.*
import com.smarttour.service.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = ["*"])
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping("/{userId}")
    fun getProfile(@PathVariable userId: UUID): ProfileResponse {
        return profileService.getProfile(userId)
    }

    @GetMapping("/{userId}/favorites")
    fun getFavorites(@PathVariable userId: UUID): List<FavoriteResponse> {
        return profileService.getFavorites(userId)
    }

    @PostMapping("/favorites")
    @ResponseStatus(HttpStatus.CREATED)
    fun addFavorite(@RequestBody request: FavoriteRequest): FavoriteResponse {
        return profileService.addFavorite(request)
    }

    @DeleteMapping("/{userId}/favorites/{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeFavorite(@PathVariable userId: UUID, @PathVariable placeId: UUID) {
        profileService.removeFavorite(userId, placeId)
    }

    @GetMapping("/{userId}/history")
    fun getVisitHistory(@PathVariable userId: UUID): List<VisitHistoryResponse> {
        return profileService.getVisitHistory(userId)
    }

    @PostMapping("/history")
    @ResponseStatus(HttpStatus.CREATED)
    fun recordVisit(@RequestBody request: VisitRequest): VisitHistoryResponse {
        return profileService.recordVisit(request)
    }
}
