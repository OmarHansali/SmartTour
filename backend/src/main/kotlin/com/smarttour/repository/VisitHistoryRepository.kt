package com.smarttour.repository

import com.smarttour.model.VisitHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VisitHistoryRepository : JpaRepository<VisitHistory, UUID> {
    fun findByUserIdOrderByVisitedAtDesc(userId: UUID): List<VisitHistory>
    fun findByUserIdAndPlaceId(userId: UUID, placeId: UUID): List<VisitHistory>
    fun countByUserId(userId: UUID): Long
}
