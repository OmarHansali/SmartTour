package com.smarttour.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pois")
data class PoiEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val rating: Double,
    val distanceMeters: Double,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false
)

@Entity(tableName = "tours")
data class TourEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val rating: Double,
    val imageUrl: String? = null,
    val isSaved: Boolean = false
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String? = null
)
