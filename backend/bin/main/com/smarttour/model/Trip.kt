package com.smarttour.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "trips")
data class Trip(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @Column(nullable = false)
    val name: String,
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "trip_places",
        joinColumns = [JoinColumn(name = "trip_id")],
        inverseJoinColumns = [JoinColumn(name = "place_id")]
    )
    val places: MutableList<Place> = mutableListOf(),
    
    @ElementCollection
    @CollectionTable(name = "trip_route_points", joinColumns = [JoinColumn(name = "trip_id")])
    val optimizedRoute: MutableList<RoutePoint> = mutableListOf(),
    
    val estimatedDurationMinutes: Int? = null,
    
    val estimatedDistanceMeters: Int? = null,
    
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = null,
        user = User(),
        name = "",
        places = mutableListOf(),
        optimizedRoute = mutableListOf(),
        estimatedDurationMinutes = null,
        estimatedDistanceMeters = null,
        createdAt = LocalDateTime.now()
    )
}

@Embeddable
data class RoutePoint(
    val latitude: Double,
    val longitude: Double,
    @Column(name = "route_order")
    val order: Int,
    val placeName: String? = null
) {
    constructor() : this(0.0, 0.0, 0, null)
}
