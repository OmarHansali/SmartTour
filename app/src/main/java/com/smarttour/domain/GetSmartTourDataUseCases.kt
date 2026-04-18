package com.smarttour.domain

import com.smarttour.data.SmartTourRepository
import javax.inject.Inject

class GetExploreFeedUseCase @Inject constructor(private val repository: SmartTourRepository) {
    suspend operator fun invoke(): ExploreFeed = repository.getExploreFeed()
}

class GetPoiDetailUseCase @Inject constructor(private val repository: SmartTourRepository) {
    suspend operator fun invoke(poiId: String): PoiDetail = repository.getPoiDetail(poiId)
}

class GetTourMapUseCase @Inject constructor(private val repository: SmartTourRepository) {
    suspend operator fun invoke(): TourMap = repository.getTourMap()
}

class GetArCameraUseCase @Inject constructor(private val repository: SmartTourRepository) {
    suspend operator fun invoke(poiId: String?): ArCamera = repository.getArCamera(poiId)
}

class GetProfileUseCase @Inject constructor(private val repository: SmartTourRepository) {
    suspend operator fun invoke(): Profile = repository.getProfile()
}