package com.smarttour.repository

import com.smarttour.model.Favorite
import com.smarttour.model.Place
import com.smarttour.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FavoriteRepository : JpaRepository<Favorite, UUID> {
    fun findByUser(user: User): List<Favorite>
    fun findByUserId(userId: UUID): List<Favorite>
    fun existsByUserIdAndPlaceId(userId: UUID, placeId: UUID): Boolean
    fun deleteByUserIdAndPlaceId(userId: UUID, placeId: UUID)
}
