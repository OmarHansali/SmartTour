package com.smarttour.service

import com.smarttour.config.GoogleMapsConfig
import com.smarttour.model.Place
import com.smarttour.model.PlaceCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class GoogleMapsClient(
    private val webClient: WebClient,
    private val config: GoogleMapsConfig
) {

    suspend fun searchNearbyPlaces(latitude: Double, longitude: Double, radiusMeters: Int): List<Place> {
        return withContext(Dispatchers.IO) {
            try {
                val response = webClient.get()
                    .uri { builder ->
                        builder.path("/place/nearbysearch/json")
                            .queryParam("location", "$latitude,$longitude")
                            .queryParam("radius", radiusMeters)
                            .queryParam("key", config.getApiKey())
                            .build()
                    }
                    .retrieve()
                    .awaitBody<Map<String, Any>>()
                
                parsePlacesResponse(response)
            } catch (e: Exception) {
                // Return mock data if API call fails or key not configured
                generateMockPlaces(latitude, longitude)
            }
        }
    }
    
    suspend fun getDirections(origin: String, destination: String, waypoints: List<String>? = null): Map<String, Any>? {
        return withContext(Dispatchers.IO) {
            try {
                val uriBuilder = { builder: org.springframework.web.util.UriBuilder ->
                    builder.path("/directions/json")
                        .queryParam("origin", origin)
                        .queryParam("destination", destination)
                        .queryParam("mode", "walking")
                        .queryParam("key", config.getApiKey())
                        .apply {
                            if (!waypoints.isNullOrEmpty()) {
                                queryParam("waypoints", waypoints.joinToString("|"))
                            }
                        }
                        .build()
                }
                
                webClient.get()
                    .uri(uriBuilder)
                    .retrieve()
                    .awaitBody<Map<String, Any>>()
            } catch (e: Exception) {
                null
            }
        }
    }
    
    private fun parsePlacesResponse(response: Map<String, Any>): List<Place> {
        val results = response["results"] as? List<Map<String, Any>> ?: return emptyList()
        
        return results.map { place ->
            val geometry = place["geometry"] as? Map<String, Any>
            val location = geometry?.get("location") as? Map<String, Any>
            val types = place["types"] as? List<String>
            
            Place(
                name = place["name"] as? String ?: "Unknown",
                description = place["vicinity"] as? String,
                latitude = location?.get("lat") as? Double ?: 0.0,
                longitude = location?.get("lng") as? Double ?: 0.0,
                category = mapGoogleTypeToCategory(types?.firstOrNull()),
                rating = place["rating"] as? Double,
                address = place["vicinity"] as? String,
                googlePlaceId = place["place_id"] as? String
            )
        }
    }
    
    private fun mapGoogleTypeToCategory(googleType: String?): PlaceCategory {
        return when (googleType) {
            "restaurant" -> PlaceCategory.RESTAURANT
            "cafe" -> PlaceCategory.CAFE
            "museum" -> PlaceCategory.MUSEUM
            "park" -> PlaceCategory.PARK
            "shopping_mall", "store" -> PlaceCategory.SHOPPING
            "tourist_attraction", "point_of_interest" -> PlaceCategory.LANDMARK
            "church", "mosque", "synagogue", "hindu_temple" -> PlaceCategory.RELIGIOUS
            "natural_feature" -> PlaceCategory.NATURE
            "amusement_park", "movie_theater" -> PlaceCategory.ENTERTAINMENT
            else -> PlaceCategory.OTHER
        }
    }
    
    private fun generateMockPlaces(lat: Double, lng: Double): List<Place> {
        val mockData = listOf(
            Triple("Central Museum", PlaceCategory.MUSEUM, Pair(0.001, 0.002)),
            Triple("Riverside Park", PlaceCategory.PARK, Pair(0.002, -0.001)),
            Triple("Old Town Cafe", PlaceCategory.CAFE, Pair(-0.001, 0.001)),
            Triple("Grand Cathedral", PlaceCategory.RELIGIOUS, Pair(0.003, 0.003)),
            Triple("Shopping District", PlaceCategory.SHOPPING, Pair(-0.002, -0.002)),
            Triple("Historic Tower", PlaceCategory.LANDMARK, Pair(0.0015, -0.0015)),
            Triple("Botanical Gardens", PlaceCategory.NATURE, Pair(-0.003, 0.002)),
            Triple("Art Gallery", PlaceCategory.MUSEUM, Pair(0.0025, 0.001)),
            Triple("City Theater", PlaceCategory.ENTERTAINMENT, Pair(-0.0015, -0.003)),
            Triple("Mountain View Restaurant", PlaceCategory.RESTAURANT, Pair(0.004, -0.001))
        )
        
        return mockData.map { (name, category, offset) ->
            Place(
                name = name,
                description = "A popular $category in the area with excellent reviews from visitors.",
                latitude = lat + offset.first,
                longitude = lng + offset.second,
                category = category,
                rating = 3.5 + Math.random() * 1.5,
                address = "123 Sample Street, Near $name",
                imageUrl = "https://picsum.photos/400/300?random=${(Math.random() * 1000).toInt()}"
            )
        }
    }
}
