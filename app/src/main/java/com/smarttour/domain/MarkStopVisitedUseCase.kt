package com.smarttour.domain

class MarkStopVisitedUseCase(
    private val repository: MapRepository
) {
    suspend operator fun invoke(tourId: String, stopId: String) {
        repository.markStopVisited(tourId, stopId)
    }
}
