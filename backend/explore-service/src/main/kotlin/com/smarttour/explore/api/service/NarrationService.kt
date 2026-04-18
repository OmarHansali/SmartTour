package com.smarttour.explore.api.service

import com.smarttour.explore.domain.entity.Narration
import com.smarttour.explore.domain.repository.NarrationRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux
import java.time.LocalDateTime

data class NarrationRequest(
    val poiId: String,
    val language: String = "en",
    val text: String
)

@Service
class NarrationService(
    private val narrationRepository: NarrationRepository
) {
    
    fun generateNarrationAsync(
        poiId: String,
        language: String,
        text: String
    ): Mono<Narration> {
        return Mono.fromCallable {
            // Simulate AI narration generation
            Thread.sleep(1000) // Simulate processing
            
            val narration = Narration(
                poi = null,
                language = language,
                text = text,
                audioUrl = "https://example.com/narration/$poiId/$language.mp3",
                duration = (text.length / 150) * 60, // Rough estimate of seconds
                narrator = "ai-narrator",
                createdAt = LocalDateTime.now()
            )
            narrationRepository.save(narration)
        }.subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
    }
    
    fun getNarrationsByPoiId(poiId: String): Flux<Narration> {
        return Flux.fromIterable(narrationRepository.findByPoiId(poiId))
            .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
    }
    
    fun getNarrationByPoiAndLanguage(poiId: String, language: String): Mono<Narration?> {
        return Mono.fromCallable {
            narrationRepository.findByPoiIdAndLanguage(poiId, language)
        }.subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
    }
}
