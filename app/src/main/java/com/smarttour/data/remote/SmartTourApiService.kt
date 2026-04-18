package com.smarttour.data.remote

import com.smarttour.domain.model.ExploreFeedResponse
import com.smarttour.domain.model.PoiDetailResponse
import com.smarttour.domain.model.TourMapResponse
import com.smarttour.domain.model.ArCameraResponse
import com.smarttour.domain.model.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SmartTourApiService {
    @GET("api/explore")
    suspend fun getExploreFeed(): ExploreFeedResponse

    @GET("api/pois/{poiId}")
    suspend fun getPoiDetail(@Path("poiId") poiId: String): PoiDetailResponse

    @GET("api/tours/active")
    suspend fun getActiveTour(): TourMapResponse

    @GET("api/ar")
    suspend fun getArCamera(@Query("poiId") poiId: String? = null): ArCameraResponse

    @GET("api/profile")
    suspend fun getProfile(): ProfileResponse
}

typealias SmartTourApi = SmartTourApiService
