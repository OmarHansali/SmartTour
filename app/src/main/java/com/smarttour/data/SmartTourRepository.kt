package com.smarttour.data

import com.smarttour.domain.ArCamera
import com.smarttour.domain.ExploreFeed
import com.smarttour.domain.PoiDetail
import com.smarttour.domain.Profile
import com.smarttour.domain.TourMap
import com.smarttour.network.RetrofitInstance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmartTourRepository @Inject constructor() {

    suspend fun getExploreFeed(): ExploreFeed = RetrofitInstance.api.getExploreFeed().toDomain()
    suspend fun getPoiDetail(poiId: String): PoiDetail = RetrofitInstance.api.getPoiDetail(poiId).toDomain()
    suspend fun getTourMap(): TourMap = RetrofitInstance.api.getTourMap().toDomain()
    suspend fun getArCamera(poiId: String?): ArCamera = RetrofitInstance.api.getArCamera(poiId).toDomain()
    suspend fun getProfile(): Profile = RetrofitInstance.api.getProfile().toDomain()
}