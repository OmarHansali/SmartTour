package com.smarttour.explore.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "narrations")
data class Narration(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poi_id")
    val poi: PointOfInterest? = null,
    
    val language: String = "en",
    val text: String = "",
    val audioUrl: String? = null,
    val duration: Int = 0,
    val narrator: String = "default",
    
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
