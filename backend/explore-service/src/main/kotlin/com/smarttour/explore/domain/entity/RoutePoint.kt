package com.smarttour.explore.domain.entity

import jakarta.persistence.*
import org.locationtech.jts.geom.Point

@Entity
@Table(name = "route_points")
data class RoutePoint(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    val tour: Tour? = null,
    
    val sequence: Int = 0,
    val name: String = "",
    val description: String = "",
    
    @Column(columnDefinition = "geometry(Point,4326)")
    val location: Point? = null,
    
    val estimatedWaitMinutes: Int = 0,
    val distanceFromPrevious: Double = 0.0
)
