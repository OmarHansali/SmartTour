package com.smarttour.explore.domain.repository

import com.smarttour.explore.domain.entity.Tour
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TourRepository : JpaRepository<Tour, String> {
    
    fun findByIsPublishedTrue(): List<Tour>
    
    @Query("SELECT t FROM Tour t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    fun searchByTitle(query: String): List<Tour>
    
    fun findByDifficultyOrderByRatingDesc(difficulty: String): List<Tour>
    
    fun findTop10ByIsPublishedTrueOrderByRatingDesc(): List<Tour>
}
