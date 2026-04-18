package com.smarttour.network

import com.smarttour.data.TourRouteDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MapApiService {

    @GET("/api/tours/{tourId}/route")
    suspend fun getActiveTourRoute(@Path("tourId") tourId: String): TourRouteDto

    @POST("/api/tours/{tourId}/stops/{stopId}/visit")
    suspend fun markStopVisited(
        @Path("tourId") tourId: String,
        @Path("stopId") stopId: String
    )
}
