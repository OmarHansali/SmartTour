package com.smarttour.network

import retrofit2.http.GET

interface POIApi {

    @GET("/api/pois")
    suspend fun getPOIs(): List<POIDto>
}