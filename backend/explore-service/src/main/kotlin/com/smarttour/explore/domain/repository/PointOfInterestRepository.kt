package com.smarttour.explore.domain.repository

import com.smarttour.explore.domain.entity.PointOfInterest
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PointOfInterestRepository : JpaRepository<PointOfInterest, String> {
    
    fun findByCategory(category: String): List<PointOfInterest>
    
    /**
     * Find nearby POIs using PostGIS native query
     * Uses parameterized query to prevent SQL injection
     * @param latitude Latitude of search center
     * @param longitude Longitude of search center
     * @param radiusMeters Search radius in meters
     */
    @Query(
        value = """
            SELECT * FROM pois 
            WHERE ST_DWithin(
                location, 
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :radiusMeters
            )
            ORDER BY location <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geometry
        """,
        nativeQuery = true
    )
    fun findNearby(
        @Param("latitude") latitude: Double,
        @Param("longitude") longitude: Double,
        @Param("radiusMeters") radiusMeters: Double
    ): List<PointOfInterest>
    
    /**
     * Search POIs by name using parameterized query
     * Prevents SQL injection through parameter binding
     */
    @Query(
        value = """
            SELECT * FROM pois 
            WHERE LOWER(name) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY name
        """,
        nativeQuery = true
    )
    fun searchByName(@Param("query") query: String): List<PointOfInterest>
    
    fun findTop10ByOrderByRatingDesc(): List<PointOfInterest>
}
