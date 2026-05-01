package com.smarttour.controller

import com.smarttour.dto.ARAnalysisRequest
import com.smarttour.dto.ARAnalysisResponse
import com.smarttour.service.ARService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ar")
@CrossOrigin(origins = ["*"])
class ARController(
    private val arService: ARService
) {

    @PostMapping("/analyze")
    fun analyzeScene(@RequestBody request: ARAnalysisRequest): ARAnalysisResponse {
        return arService.analyzeARScene(request)
    }
}
