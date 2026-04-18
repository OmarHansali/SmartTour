package com.smarttour.explore.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.config.CorsRegistry

@Configuration
class WebFluxConfig : WebFluxConfigurer {
    
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*")
            .maxAge(3600)
    }
}
