package com.smarttour

import com.smarttour.data.MapRepositoryImpl
import com.smarttour.data.POIRepository
import com.smarttour.domain.GetActiveTourMapUseCase
import com.smarttour.domain.GetPOIsUseCase
import com.smarttour.domain.MarkStopVisitedUseCase
import com.smarttour.network.RetrofitInstance

object AppContainer {

    private val poiRepository = POIRepository()
    val getPOIsUseCase = GetPOIsUseCase(poiRepository)

    private val mapRepository = MapRepositoryImpl(RetrofitInstance.mapApi)
    val getActiveTourMapUseCase = GetActiveTourMapUseCase(mapRepository)
    val markStopVisitedUseCase = MarkStopVisitedUseCase(mapRepository)
}