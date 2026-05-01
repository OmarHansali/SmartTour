package com.smarttour.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "places")
data class Place(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(length = 2000)
    val description: String? = null,
    
    @Column(nullable = false)
    val latitude: Double,
    
    @Column(nullable = false)
    val longitude: Double,
    
    @Enumerated(EnumType.STRING)
    val category: PlaceCategory? = PlaceCategory.OTHER,
    
    val rating: Double? = null,
    
    val imageUrl: String? = null,
    
    val address: String? = null,
    
    val googlePlaceId: String? = null,
    
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = null,
        name = "",
        description = null,
        latitude = 0.0,
        longitude = 0.0,
        category = PlaceCategory.OTHER,
        rating = null,
        imageUrl = null,
        address = null,
        googlePlaceId = null,
        createdAt = LocalDateTime.now()
    )
}

enum class PlaceCategory {
    RESTAURANT, CAFE, MUSEUM, PARK, HISTORIC, SHOPPING, 
    ENTERTAINMENT, NATURE, LANDMARK, RELIGIOUS, OTHER
}
