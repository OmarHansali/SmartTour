package com.smarttour.dto

import com.smarttour.model.Place

fun Place.toSummaryDto(): PlaceSummaryDto = PlaceSummaryDto(
    id = this.id,
    name = this.name,
    category = this.category?.name,
    imageUrl = this.imageUrl,
    address = this.address,
    rating = this.rating
)
