package com.smarttour.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "visit_history")
data class VisitHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    val place: Place,
    
    val visitedAt: LocalDateTime = LocalDateTime.now(),
    
    val userRating: Double? = null,
    
    @Column(length = 1000)
    val notes: String? = null
) {
    constructor() : this(
        id = null,
        user = User(),
        place = Place(),
        visitedAt = LocalDateTime.now(),
        userRating = null,
        notes = null
    )
}
