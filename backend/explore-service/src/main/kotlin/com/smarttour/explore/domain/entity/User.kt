package com.smarttour.explore.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",
    
    @Column(unique = true)
    val email: String = "",
    
    val passwordHash: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val profilePictureUrl: String? = null,
    
    val isActive: Boolean = true,
    
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val preferences: MutableList<UserPreference> = mutableListOf(),
    
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val savedTours: MutableList<SavedTour> = mutableListOf()
)
