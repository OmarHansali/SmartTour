package com.smarttour.explore.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_preferences")
data class UserPreference(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,
    
    val category: String = "",
    val value: String = ""
)

@Entity
@Table(name = "saved_tours")
data class SavedTour(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    val tour: Tour? = null,
    
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false
)
