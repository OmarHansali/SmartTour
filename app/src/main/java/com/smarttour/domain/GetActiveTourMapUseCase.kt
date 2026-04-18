package com.smarttour.domain

class GetActiveTourMapUseCase(
    private val repository: MapRepository
) {
    suspend operator fun invoke(tourId: String): TourRoute {
        return repository.getActiveTourRoute(tourId)
    }
}
