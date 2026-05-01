package com.smarttour.service

import com.smarttour.dto.*
import com.smarttour.model.Place
import com.smarttour.model.PlaceCategory
import com.smarttour.repository.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.math.*

@Service
class MapsService(
    private val placeRepository: PlaceRepository,
    private val googleMapsClient: GoogleMapsClient
) {

    fun findNearbyPlaces(latitude: Double, longitude: Double, radiusMeters: Int): NearbyPlacesResponse {
        val places = placeRepository.findNearbyPlaces(latitude, longitude, radiusMeters.toDouble())
        
        val placeDtos = places.map { place ->
            val distance = calculateDistance(latitude, longitude, place.latitude, place.longitude)
            NearbyPlaceDto(
                id = place.id,
                name = place.name,
                description = place.description,
                latitude = place.latitude,
                longitude = place.longitude,
                category = place.category?.name,
                rating = place.rating,
                imageUrl = place.imageUrl,
                address = place.address,
                distanceMeters = distance
            )
        }
        
        return NearbyPlacesResponse(
            places = placeDtos,
            userLatitude = latitude,
            userLongitude = longitude,
            totalFound = placeDtos.size
        )
    }
    
    suspend fun searchNearbyPlacesFromGoogle(latitude: Double, longitude: Double, radiusMeters: Int): NearbyPlacesResponse {
        return withContext(Dispatchers.IO) {
            val googlePlaces = googleMapsClient.searchNearbyPlaces(latitude, longitude, radiusMeters)
            
            val savedPlaces = googlePlaces.map { place ->
                val existing = place.googlePlaceId?.let { placeRepository.findByGooglePlaceId(it) }
                existing ?: placeRepository.save(place)
            }
            
            val placeDtos = savedPlaces.map { place ->
                val distance = calculateDistance(latitude, longitude, place.latitude, place.longitude)
                NearbyPlaceDto(
                    id = place.id,
                    name = place.name,
                    description = place.description,
                    latitude = place.latitude,
                    longitude = place.longitude,
                    category = place.category?.name,
                    rating = place.rating,
                    imageUrl = place.imageUrl,
                    address = place.address,
                    distanceMeters = distance
                )
            }
            
            NearbyPlacesResponse(
                places = placeDtos,
                userLatitude = latitude,
                userLongitude = longitude,
                totalFound = placeDtos.size
            )
        }
    }
    
    fun getPlaceById(id: UUID): Place? {
        return placeRepository.findById(id).orElse(null)
    }
    
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000.0 // Earth radius in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
    
    fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLon = Math.toRadians(lon2 - lon1)
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        
        val y = sin(dLon) * cos(lat2Rad)
        val x = cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(dLon)
        
        val bearing = Math.toDegrees(atan2(y, x))
        return (bearing + 360) % 360
    }
    
    fun getDirectionInstruction(bearing: Double): String {
        return when (bearing) {
            in 337.5..360.0, in 0.0..22.5 -> "Head North"
            in 22.5..67.5 -> "Head Northeast"
            in 67.5..112.5 -> "Head East"
            in 112.5..157.5 -> "Head Southeast"
            in 157.5..202.5 -> "Head South"
            in 202.5..247.5 -> "Head Southwest"
            in 247.5..292.5 -> "Head West"
            in 292.5..337.5 -> "Head Northwest"
            else -> "Head North"
        }
    }
}
