package com.smarttour.network

import com.smarttour.data.ArCameraDto
import com.smarttour.data.ExploreFeedDto
import com.smarttour.data.PoiDetailDto
import com.smarttour.data.ProfileDto
import com.smarttour.data.TourMapDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SmartTourApi {

    @GET("/api/explore")
    suspend fun getExploreFeed(): ExploreFeedDto

    @GET("/api/pois/{poiId}")
    suspend fun getPoiDetail(@Path("poiId") poiId: String): PoiDetailDto

    @GET("/api/tours/active")
    suspend fun getTourMap(): TourMapDto

    @GET("/api/ar")
    suspend fun getArCamera(@Query("poiId") poiId: String?): ArCameraDto

    @GET("/api/profile")
    suspend fun getProfile(): ProfileDto
}
