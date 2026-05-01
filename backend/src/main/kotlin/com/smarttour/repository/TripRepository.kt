package com.smarttour.repository

import com.smarttour.model.Trip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TripRepository : JpaRepository<Trip, UUID> {
    fun findByUserId(userId: UUID): List<Trip>
}
