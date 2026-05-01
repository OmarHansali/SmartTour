package com.smarttour.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false, unique = true)
    val email: String,
    
    val avatarUrl: String? = null,
    
    @ElementCollection
    @CollectionTable(name = "user_preferences", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "preference")
    val preferences: MutableSet<String> = mutableSetOf(),
    
    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = null,
        name = "",
        email = "",
        avatarUrl = null,
        preferences = mutableSetOf(),
        createdAt = LocalDateTime.now()
    )
}
