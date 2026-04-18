package com.smarttour.explore.domain.repository

import com.smarttour.explore.domain.entity.Narration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NarrationRepository : JpaRepository<Narration, String> {
    fun findByPoiId(poiId: String): List<Narration>
    fun findByPoiIdAndLanguage(poiId: String, language: String): Narration?
}
