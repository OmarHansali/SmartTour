package com.smarttour.explore.domain.entity

import jakarta.persistence.*
import org.locationtech.jts.geom.Point
import java.time.LocalDateTime

@Entity
@Table(name = "pois", indexes = [Index(name = "idx_location", columnList = "location")])
data class PointOfInterest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",
    
    val name: String = "",
    val description: String = "",
    val shortDescription: String = "",
    val category: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val imageUrl: String? = null,
    
    @Column(columnDefinition = "geometry(Point,4326)")
    val location: Point? = null,
    
    val distanceMeters: Double = 0.0,
    val openingHours: String = "",
    val entryFee: String = "",
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    val createdBy: User? = null,
    
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @OneToMany(mappedBy = "poi", cascade = [CascadeType.ALL], orphanRemoval = true)
    val narrations: MutableList<Narration> = mutableListOf(),
    
    @ManyToMany(mappedBy = "pointsOfInterest")
    val tours: MutableList<Tour> = mutableListOf()
)
