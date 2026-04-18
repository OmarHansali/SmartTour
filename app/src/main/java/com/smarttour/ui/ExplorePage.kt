package com.smarttour.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttour.domain.ExploreFeed

@Composable
internal fun ExplorePage(
    feed: ExploreFeed,
    onPoiClick: (String) -> Unit,
    onTourClick: (String) -> Unit,
    onOpenAr: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ExploreBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(text = feed.greeting, color = AccentBlue, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = feed.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = PrimaryInk,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))
                RoundedSearchBar(feed.searchPlaceholder)
                Spacer(modifier = Modifier.height(12.dp))
                ExploreQuickActions(
                    onOpenAr = onOpenAr,
                    onOpenMap = { onTourClick(feed.featuredTours.firstOrNull()?.id ?: "") },
                    city = feed.city
                )
            }
        }

        item { SectionTitle("Nearby highlights") }
        items(feed.nearbyHighlights) { poi ->
            NearbyHighlightCard(poi = poi, onClick = { onPoiClick(poi.id) })
        }

        item { SectionTitle("Featured tours") }
        items(feed.featuredTours) { tour ->
            FeaturedTourCard(tour = tour, onClick = { onTourClick(tour.id) })
        }
    }
}
