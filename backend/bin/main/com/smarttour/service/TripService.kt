package com.smarttour.service

import com.smarttour.dto.*
import com.smarttour.model.Place
import com.smarttour.model.Trip
import com.smarttour.model.RoutePoint
import com.smarttour.model.User
import com.smarttour.repository.PlaceRepository
import com.smarttour.repository.TripRepository
import com.smarttour.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.math.*

@Service
class TripService(
    private val tripRepository: TripRepository,
    private val placeRepository: PlaceRepository,
    private val userRepository: UserRepository,
    private val mapsService: MapsService
) {

    @Transactional
    fun generateTrip(request: TripRequest): TripResponse {
        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        
        val places = findOptimalPlaces(
            request.latitude, 
            request.longitude, 
            request.radiusMeters,
            request.maxPlaces,
            request.categories
        )
        
        val optimizedPlaces = optimizeRoute(request.latitude, request.longitude, places)
        
        val route = optimizedPlaces.mapIndexed { index, place ->
            RoutePoint(
                latitude = place.latitude,
                longitude = place.longitude,
                order = index + 1,
                placeName = place.name
            )
        }
        
        val totalDistance = calculateTotalDistance(request.latitude, request.longitude, optimizedPlaces)
        val totalDuration = estimateTotalDuration(optimizedPlaces)
        
        val trip = Trip(
            user = user,
            name = request.name ?: generateTripName(optimizedPlaces),
            places = optimizedPlaces.toMutableList(),
            optimizedRoute = route.toMutableList(),
            estimatedDurationMinutes = totalDuration,
            estimatedDistanceMeters = totalDistance.toInt()
        )
        
        val savedTrip = tripRepository.save(trip)
        
        return mapToTripResponse(savedTrip, optimizedPlaces)
    }
    
    fun getUserTrips(userId: UUID): List<TripResponse> {
        return tripRepository.findByUserId(userId).map { trip ->
            mapToTripResponse(trip, trip.places)
        }
    }
    
    fun getTripById(tripId: UUID): TripResponse? {
        return tripRepository.findById(tripId).map { trip ->
            mapToTripResponse(trip, trip.places)
        }.orElse(null)
    }
    
    private fun findOptimalPlaces(
        latitude: Double, 
        longitude: Double, 
        radiusMeters: Int,
        maxPlaces: Int,
        categories: List<String>?
    ): List<Place> {
        val allPlaces = placeRepository.findNearbyPlaces(latitude, longitude, radiusMeters.toDouble())
        
        val filtered = if (!categories.isNullOrEmpty()) {
            val categoryEnums = categories.mapNotNull { 
                try { enumValueOf<com.smarttour.model.PlaceCategory>(it.uppercase()) } 
                catch (e: Exception) { null }
            }
            allPlaces.filter { it.category in categoryEnums }
        } else {
            allPlaces
        }
        
        return filtered
            .sortedByDescending { it.rating ?: 0.0 }
            .take(maxPlaces)
    }
    
    private fun optimizeRoute(startLat: Double, startLng: Double, places: List<Place>): List<Place> {
        if (places.size <= 2) return places
        
        // Nearest neighbor algorithm for TSP-like route optimization
        val unvisited = places.toMutableList()
        val route = mutableListOf<Place>()
        var currentLat = startLat
        var currentLng = startLng
        
        while (unvisited.isNotEmpty()) {
            val nearest = unvisited.minByOrNull { 
                mapsService.calculateDistance(currentLat, currentLng, it.latitude, it.longitude)
            } ?: break
            
            route.add(nearest)
            unvisited.remove(nearest)
            currentLat = nearest.latitude
            currentLng = nearest.longitude
        }
        
        return route
    }
    
    private fun calculateTotalDistance(startLat: Double, startLng: Double, places: List<Place>): Double {
        if (places.isEmpty()) return 0.0
        
        var total = mapsService.calculateDistance(startLat, startLng, places.first().latitude, places.first().longitude)
        
        for (i in 0 until places.size - 1) {
            total += mapsService.calculateDistance(
                places[i].latitude, places[i].longitude,
                places[i + 1].latitude, places[i + 1].longitude
            )
        }
        
        return total
    }
    
    private fun estimateTotalDuration(places: List<Place>): Int {
        val travelTimePerKm = 12 // minutes per km (walking)
        val visitTimePerPlace = 45 // minutes
        
        var totalDistance = 0.0
        if (places.size >= 2) {
            for (i in 0 until places.size - 1) {
                totalDistance += mapsService.calculateDistance(
                    places[i].latitude, places[i].longitude,
                    places[i + 1].latitude, places[i + 1].longitude
                )
            }
        }
        
        val travelMinutes = (totalDistance / 1000 * travelTimePerKm).toInt()
        val visitMinutes = places.size * visitTimePerPlace
        
        return travelMinutes + visitMinutes
    }
    
    private fun generateTripName(places: List<Place>): String {
        return if (places.size >= 2) {
            "${places[0].name} & ${places[1].name} Tour"
        } else if (places.size == 1) {
            "${places[0].name} Visit"
        } else {
            "Custom Tour"
        }
    }
    
    private fun mapToTripResponse(trip: Trip, orderedPlaces: List<Place>): TripResponse {
        val placeDtos = orderedPlaces.mapIndexed { index, place ->
            PlaceInTripDto(
                id = place.id,
                name = place.name,
                description = place.description,
                latitude = place.latitude,
                longitude = place.longitude,
                category = place.category?.name,
                rating = place.rating,
                imageUrl = place.imageUrl,
                address = place.address,
                order = index + 1
            )
        }
        
        val routeDtos = trip.optimizedRoute.map { 
            RoutePointDto(
                latitude = it.latitude,
                longitude = it.longitude,
                order = it.order,
                placeName = it.placeName
            )
        }
        
        return TripResponse(
            id = trip.id,
            name = trip.name,
            places = placeDtos,
            route = routeDtos,
            estimatedDurationMinutes = trip.estimatedDurationMinutes,
            estimatedDistanceMeters = trip.estimatedDistanceMeters,
            totalStops = placeDtos.size
        )
    }
}
