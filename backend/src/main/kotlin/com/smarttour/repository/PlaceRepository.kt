package com.smarttour.repository

import com.smarttour.model.Place
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlaceRepository : JpaRepository<Place, UUID> {
    @Query(
        value = """
            SELECT p FROM Place p 
            WHERE (6371000 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * 
            cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(p.latitude)))) <= :radiusMeters
            ORDER BY (6371000 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * 
            cos(radians(p.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(p.latitude)))) ASC
        """
    )
    fun findNearbyPlaces(
        @Param("lat") latitude: Double,
        @Param("lng") longitude: Double,
        @Param("radiusMeters") radiusMeters: Double
    ): List<Place>
    
    fun findByCategory(category: com.smarttour.model.PlaceCategory): List<Place>
    fun findByGooglePlaceId(googlePlaceId: String): Place?
}
