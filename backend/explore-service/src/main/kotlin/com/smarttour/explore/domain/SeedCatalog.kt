package com.smarttour.explore.domain

import org.springframework.stereotype.Component

@Component
class SeedCatalog {

    val pois: List<SeedPoi> = listOf(
        SeedPoi(
            id = "poi-koutoubia",
            name = "Koutoubia Mosque",
            shortDescription = "The iconic Almohad mosque and minaret in the heart of Marrakech.",
            detailDescription = "The Koutoubia Mosque is the largest mosque in Marrakech, built in the 12th century under the Almohad dynasty. Its landmark minaret became a model for later towers across the western Islamic world and remains one of the city's most recognizable symbols.",
            category = "Mosque",
            distanceMeters = 320,
            rating = 4.8,
            latitude = 31.6236,
            longitude = -7.9936,
            subtitle = "Religious site - Marrakech Medina",
            status = "Open",
            admissionLabel = "Free",
            eraLabel = "12th century",
            arSubtitle = "Built 1158 - Almohad dynasty",
            narrationTitle = "Koutoubia narration",
            narrationSubtitle = "Tap to hear the full story"
        ),
        SeedPoi(
            id = "poi-bahia",
            name = "Bahia Palace",
            shortDescription = "A 19th century palace known for its gardens and decorated courtyards.",
            detailDescription = "Bahia Palace is one of Marrakech's finest historic residences, combining intimate courtyards, carved cedar ceilings, and colorful tilework. The site reflects elite Moroccan domestic architecture from the late 19th century.",
            category = "Palace",
            distanceMeters = 650,
            rating = 4.6,
            latitude = 31.6216,
            longitude = -7.9833,
            subtitle = "Historic palace - Marrakech Medina",
            status = "Open",
            admissionLabel = "Paid",
            eraLabel = "19th century",
            arSubtitle = "Grand residence - ornate courtyards",
            narrationTitle = "Bahia Palace narration",
            narrationSubtitle = "A guided palace story is ready"
        ),
        SeedPoi(
            id = "poi-jemaa",
            name = "Jemaa el-Fna",
            shortDescription = "A lively historic square filled with food, music, and street performances.",
            detailDescription = "Jemaa el-Fna is the social stage of Marrakech, where food stalls, musicians, storytellers, and traders animate the city throughout the day and deep into the evening. It is both a meeting point and a cultural landmark.",
            category = "Square",
            distanceMeters = 1200,
            rating = 4.9,
            latitude = 31.6258,
            longitude = -7.9891,
            subtitle = "Public square - Marrakech Medina",
            status = "Open",
            admissionLabel = "Free",
            eraLabel = "Historic quarter",
            arSubtitle = "Cultural square - city gathering point",
            narrationTitle = "Jemaa el-Fna narration",
            narrationSubtitle = "Hear the living story of the square"
        )
    )

    val tours: List<SeedTour> = listOf(
        SeedTour(
            id = "tour-old-medina",
            title = "Old Medina Walk",
            description = "A walking route through Marrakech's historic core.",
            durationMinutes = 90,
            stopCount = 8,
            accentColorHex = "#1D9E75",
            progressLabel = "Stop 3 of 8 - 1.2 km left",
            stopPoiIds = listOf("poi-koutoubia", "poi-bahia", "poi-jemaa")
        ),
        SeedTour(
            id = "tour-souks-crafts",
            title = "Souks & Crafts",
            description = "A shorter route focused on handmade goods and artisan stories.",
            durationMinutes = 60,
            stopCount = 5,
            accentColorHex = "#534AB7",
            progressLabel = "Stop 2 of 5 - 650 m left",
            stopPoiIds = listOf("poi-bahia", "poi-jemaa")
        )
    )

    val profile = SeedProfile(
        fullName = "Amine Karimi",
        initials = "AK",
        levelLabel = "Explorer - 12 tours completed",
        stats = listOf(
            SeedStat("12", "Tours"),
            SeedStat("34", "POIs"),
            SeedStat("4", "Cities")
        ),
        settings = listOf(
            SeedSetting("Language", "English / Arabic"),
            SeedSetting("Narration voice", "Female, calm"),
            SeedSetting("Offline packs", "Marrakech (downloaded)"),
            SeedSetting("Notifications", "Tours nearby on"),
            SeedSetting("About SmartTour", "v1.0.0")
        )
    )

    fun poiById(id: String): SeedPoi {
        return pois.firstOrNull { it.id == id } ?: pois.first()
    }

    fun activeTour(): SeedTour {
        return tours.first()
    }
}

data class SeedPoi(
    val id: String,
    val name: String,
    val shortDescription: String,
    val detailDescription: String,
    val category: String,
    val distanceMeters: Int,
    val rating: Double,
    val latitude: Double,
    val longitude: Double,
    val subtitle: String,
    val status: String,
    val admissionLabel: String,
    val eraLabel: String,
    val arSubtitle: String,
    val narrationTitle: String,
    val narrationSubtitle: String
)

data class SeedTour(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val stopCount: Int,
    val accentColorHex: String,
    val progressLabel: String,
    val stopPoiIds: List<String>
)

data class SeedProfile(
    val fullName: String,
    val initials: String,
    val levelLabel: String,
    val stats: List<SeedStat>,
    val settings: List<SeedSetting>
)

data class SeedStat(
    val value: String,
    val label: String
)

data class SeedSetting(
    val label: String,
    val value: String
)
