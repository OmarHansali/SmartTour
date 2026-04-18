package com.smarttour.explore.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tours")
data class Tour(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",
    
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val durationMinutes: Int = 0,
    val difficulty: String = "EASY",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "tour_pois",
        joinColumns = [JoinColumn(name = "tour_id")],
        inverseJoinColumns = [JoinColumn(name = "poi_id")]
    )
    val pointsOfInterest: MutableList<PointOfInterest> = mutableListOf(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    val createdBy: User? = null,
    
    val isPublished: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @OneToMany(mappedBy = "tour", cascade = [CascadeType.ALL], orphanRemoval = true)
    val routePoints: MutableList<RoutePoint> = mutableListOf()
)
