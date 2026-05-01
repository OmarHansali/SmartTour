package com.smarttour.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GoogleMapsConfig {
    
    @Value("\${smarttour.google-maps.api-key}")
    private lateinit var apiKey: String
    
    @Bean
    fun googleMapsWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://maps.googleapis.com/maps/api")
            .build()
    }
    
    fun getApiKey(): String = apiKey
}
