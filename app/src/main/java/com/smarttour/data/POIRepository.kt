package com.smarttour.data

import com.smarttour.network.RetrofitInstance
import com.smarttour.domain.POI

class POIRepository {

    suspend fun getPOIs(): List<POI> {
        return RetrofitInstance.api.getPOIs().map { it.toDomain() }
    }
}