package com.smarttour.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val api: POIApi by lazy { retrofit.create(POIApi::class.java) }

    val mapApi: MapApiService by lazy { retrofit.create(MapApiService::class.java) }
}