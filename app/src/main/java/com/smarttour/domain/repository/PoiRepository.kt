package com.smarttour.domain.repository

import com.smarttour.domain.model.PoiDetailResponse
import kotlinx.coroutines.flow.Flow

interface PoiRepository {
    suspend fun getExploreFeed(): Flow<List<PoiDetailResponse>>
    suspend fun getPoiDetail(poiId: String): PoiDetailResponse
    suspend fun getNearbyPois(latitude: Double, longitude: Double, radiusMeters: Double): Flow<List<PoiDetailResponse>>
    suspend fun searchPois(query: String): Flow<List<PoiDetailResponse>>
}
