package com.smarttour.controller

import com.smarttour.dto.TripRequest
import com.smarttour.dto.TripResponse
import com.smarttour.service.TripService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = ["*"])
class TripController(
    private val tripService: TripService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTrip(@RequestBody request: TripRequest): TripResponse {
        return tripService.generateTrip(request)
    }

    @GetMapping("/user/{userId}")
    fun getUserTrips(@PathVariable userId: UUID): List<TripResponse> {
        return tripService.getUserTrips(userId)
    }

    @GetMapping("/{tripId}")
    fun getTripById(@PathVariable tripId: UUID): TripResponse? {
        return tripService.getTripById(tripId)
    }
}
