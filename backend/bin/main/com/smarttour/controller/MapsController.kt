package com.smarttour.controller

import com.smarttour.dto.LocationRequest
import com.smarttour.dto.NearbyPlacesResponse
import com.smarttour.service.GoogleMapsClient
import com.smarttour.service.MapsService
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/maps")
@CrossOrigin(origins = ["*"])
class MapsController(
    private val mapsService: MapsService,
    private val googleMapsClient: GoogleMapsClient
) {

    @PostMapping("/nearby")
    fun getNearbyPlaces(@RequestBody request: LocationRequest): NearbyPlacesResponse {
        return mapsService.findNearbyPlaces(request.latitude, request.longitude, request.radiusMeters)
    }

    @PostMapping("/nearby/google")
    fun getNearbyPlacesFromGoogle(@RequestBody request: LocationRequest): NearbyPlacesResponse = runBlocking {
        mapsService.searchNearbyPlacesFromGoogle(request.latitude, request.longitude, request.radiusMeters)
    }

    @GetMapping("/place/{id}")
    fun getPlaceById(@PathVariable id: String): Any? {
        return mapsService.getPlaceById(java.util.UUID.fromString(id))
    }

    @PostMapping("/directions")
    fun getDirections(
        @RequestParam origin: String,
        @RequestParam destination: String,
        @RequestParam(required = false) waypoints: List<String>?
    ): Any? = runBlocking {
        googleMapsClient.getDirections(origin, destination, waypoints)
    }

    @GetMapping("/distance")
    fun getDistance(
        @RequestParam lat1: Double,
        @RequestParam lng1: Double,
        @RequestParam lat2: Double,
        @RequestParam lng2: Double
    ): Map<String, Double> {
        val distance = mapsService.calculateDistance(lat1, lng1, lat2, lng2)
        val bearing = mapsService.calculateBearing(lat1, lng1, lat2, lng2)
        return mapOf("distanceMeters" to distance, "bearing" to bearing)
    }
}
