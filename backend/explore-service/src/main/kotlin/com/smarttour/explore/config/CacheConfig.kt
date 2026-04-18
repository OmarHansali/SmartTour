package com.smarttour.explore.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.cache.annotation.EnableCaching
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(20))
        
        val cacheConfigurations = mapOf(
            "pois" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)),
            "tours" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1)),
            "profile" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)),
            "exploreFeed" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)),
            "poiDetail" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(4)),
            "tourMap" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)),
            "arCamera" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
        )
        
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }
}
