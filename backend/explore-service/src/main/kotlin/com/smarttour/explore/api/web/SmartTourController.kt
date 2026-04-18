package com.smarttour.explore.api.web

import com.smarttour.explore.api.model.*
import com.smarttour.explore.api.service.SmartTourCatalogService
import com.smarttour.explore.api.service.NarrationService
import com.smarttour.explore.api.service.NarrationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api")
class SmartTourController(
    private val smartTourCatalogService: SmartTourCatalogService,
    private val narrationService: NarrationService
) {

    @GetMapping("/explore")
    fun getExploreFeed(): ExploreFeedResponse = smartTourCatalogService.getExploreFeed()

    @GetMapping("/pois/{poiId}")
    fun getPoiDetail(@PathVariable poiId: String): PoiDetailResponse =
        smartTourCatalogService.getPoiDetail(poiId)

    @GetMapping("/tours/active")
    fun getActiveTour(): TourMapResponse = smartTourCatalogService.getTourMap()

    @GetMapping("/ar")
    fun getArCamera(@RequestParam(required = false) poiId: String?): ArCameraResponse =
        smartTourCatalogService.getArCamera(poiId)

    @GetMapping("/profile")
    fun getProfile(): ProfileResponse = smartTourCatalogService.getProfile()

    @PostMapping("/narrations/generate")
    fun generateNarration(@RequestBody request: NarrationRequest): Mono<Map<String, String>> {
        return narrationService.generateNarrationAsync(
            request.poiId,
            request.language,
            request.text
        ).map { narration ->
            mapOf(
                "narrationId" to narration.id,
                "audioUrl" to (narration.audioUrl ?: ""),
                "duration" to narration.duration.toString()
            )
        }
    }

    @GetMapping("/pois/{poiId}/narrations")
    fun getNarrationsByPoi(@PathVariable poiId: String): Flux<Map<String, String>> {
        return narrationService.getNarrationsByPoiId(poiId)
            .map { narration ->
                mapOf(
                    "id" to narration.id,
                    "language" to narration.language,
                    "audioUrl" to (narration.audioUrl ?: ""),
                    "duration" to narration.duration.toString()
                )
            }
    }

    @GetMapping("/pois/{poiId}/narrations/{language}")
    fun getNarrationByLanguage(
        @PathVariable poiId: String,
        @PathVariable language: String
    ): Mono<Map<String, String>> {
        return narrationService.getNarrationByPoiAndLanguage(poiId, language)
            .map { narration ->
                if (narration != null) {
                    mapOf(
                        "id" to (narration.id ?: ""),
                        "language" to narration.language,
                        "audioUrl" to (narration.audioUrl ?: ""),
                        "duration" to narration.duration.toString()
                    )
                } else {
                    emptyMap()
                }
            }
    }
}

@RestController
@RequestMapping("/api/auth")
class AuthController {
    // Authentication endpoints will be added in the next phase
}
