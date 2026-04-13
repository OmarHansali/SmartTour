package com.smarttour.domain

import com.smarttour.data.POIRepository

class GetPOIsUseCase(
    private val repository: POIRepository
) {

    suspend operator fun invoke(): List<POI> {
        return repository.getPOIs()
    }
}