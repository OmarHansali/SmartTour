package com.smarttour.service

import com.smarttour.dto.*
import com.smarttour.model.Place
import com.smarttour.repository.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class ARService(
    private val placeRepository: PlaceRepository,
    private val mapsService: MapsService
) {

    fun analyzeARScene(request: ARAnalysisRequest): ARAnalysisResponse {
        val detectedObjects = if (!request.detectedObjects.isNullOrEmpty()) {
            request.detectedObjects.map { obj ->
                DetectedObjectDto(
                    name = obj,
                    confidence = 0.85,
                    description = generateObjectDescription(obj)
                )
            }
        } else {
            detectObjectsFromImage(request.imageBase64)
        }
        
        val nearbyPlaces = placeRepository.findNearbyPlaces(
            request.latitude, 
            request.longitude, 
            2000.0
        )
        
        val nearestPlace = nearbyPlaces.firstOrNull()
        
        val nearestPlaceDto = nearestPlace?.let { place ->
            val distance = mapsService.calculateDistance(
                request.latitude, request.longitude, 
                place.latitude, place.longitude
            )
            val bearing = mapsService.calculateBearing(
                request.latitude, request.longitude,
                place.latitude, place.longitude
            )
            NearestPlaceDto(
                name = place.name,
                distanceMeters = distance,
                bearing = bearing,
                category = place.category?.name,
                description = place.description
            )
        }
        
        val direction = nearestPlaceDto?.let { place ->
            val heading = request.compassHeading ?: 0.0
            val arrowRotation = (place.bearing - heading + 360) % 360
            DirectionDto(
                heading = place.bearing,
                instruction = mapsService.getDirectionInstruction(place.bearing),
                arrowRotation = arrowRotation
            )
        }
        
        val description = buildDescription(detectedObjects, nearestPlaceDto)
        
        return ARAnalysisResponse(
            detectedObjects = detectedObjects,
            nearestPlace = nearestPlaceDto,
            direction = direction,
            description = description
        )
    }
    
    private fun detectObjectsFromImage(imageBase64: String?): List<DetectedObjectDto> {
        if (imageBase64 == null) return emptyList()
        
        // Simulated object detection - in production, this would call ML service
        val possibleObjects = listOf(
            "Building" to "A historic structure with classical architecture featuring columns and ornate details.",
            "Restaurant" to "A dining establishment with outdoor seating and warm lighting.",
            "Park" to "A green public space with trees, benches, and walking paths.",
            "Statue" to "A sculptural monument commemorating an important historical figure.",
            "Bridge" to "An architectural crossing spanning over water or a valley.",
            "Tower" to "A tall structure visible from a distance, possibly a landmark.",
            "Cafe" to "A cozy coffee shop with a welcoming atmosphere.",
            "Museum" to "A cultural institution housing art, history, or science exhibits."
        )
        
        return possibleObjects.shuffled().take(2).map { (name, desc) ->
            DetectedObjectDto(
                name = name,
                confidence = 0.75 + Math.random() * 0.2,
                description = desc
            )
        }
    }
    
    private fun generateObjectDescription(objectName: String): String {
        return when (objectName.lowercase()) {
            "building" -> "A multi-story structure with modern or historic architecture."
            "restaurant" -> "A food establishment with signage and seating area."
            "park" -> "A recreational green space with vegetation and walking paths."
            "statue" -> "A commemorative sculpture or artistic monument."
            "bridge" -> "A structure connecting two areas across an obstacle."
            "tower" -> "A tall vertical structure, possibly a landmark or observation point."
            "cafe" -> "A small establishment serving beverages and light meals."
            "museum" -> "A cultural building housing collections and exhibits."
            "church", "temple", "mosque" -> "A place of worship with distinctive religious architecture."
            "shop", "store" -> "A commercial establishment selling goods."
            else -> "A notable point of interest in this area."
        }
    }
    
    private fun buildDescription(objects: List<DetectedObjectDto>, nearestPlace: NearestPlaceDto?): String {
        val objectDesc = if (objects.isNotEmpty()) {
            "I can see: ${objects.joinToString(", ") { it.name }}. ${objects.first().description}"
        } else {
            "I'm not detecting any specific landmarks in the current view."
        }
        
        val placeDesc = nearestPlace?.let {
            " The nearest point of interest is ${it.name} (${it.category?.lowercase() ?: "place"}), " +
            "approximately ${"%.0f".format(it.distanceMeters)} meters ${mapsService.getDirectionInstruction(it.bearing).replace("Head ", "").lowercase()}."
        } ?: " No known places of interest were found very close by."
        
        return objectDesc + placeDesc
    }
}
