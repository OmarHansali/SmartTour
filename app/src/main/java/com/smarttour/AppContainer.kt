package com.smarttour

import com.smarttour.data.POIRepository
import com.smarttour.domain.GetPOIsUseCase

object AppContainer {

    private val repository = POIRepository()

    val getPOIsUseCase = GetPOIsUseCase(repository)
}