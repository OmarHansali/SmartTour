package com.smarttour.data

import com.smarttour.domain.POI

fun POIDto.toDomain(): POI {
    return POI(
        id = id,
        name = name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        category = category,
        imageUrl = imageUrl
    )
}