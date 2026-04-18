package com.smarttour.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smarttour.domain.TourMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TourMapPage(
    tourMap: TourMap,
    onStopClick: (String) -> Unit,
    onOpenAr: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tourMap.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = PrimaryInk,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tourMap.badge,
                    color = AccentGreen,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .background(Color(0xFFE1F5EE), RoundedCornerShape(999.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDDECCE)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.2f)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val points = tourMap.routePoints.map { Offset(it.x * size.width, it.y * size.height) }
                            for (index in 0 until points.lastIndex) {
                                drawLine(
                                    color = AccentGreen,
                                    start = points[index],
                                    end = points[index + 1],
                                    strokeWidth = 10f
                                )
                            }
                            points.forEachIndexed { index, point ->
                                drawCircle(
                                    color = when (index) {
                                        0 -> AccentGreen
                                        points.lastIndex -> AccentCoral
                                        else -> AccentPurple
                                    },
                                    radius = 16f,
                                    center = point
                                )
                            }
                        }
                        Text(
                            text = tourMap.progressLabel,
                            color = PrimaryInk,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(Color.White, RoundedCornerShape(999.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStopClick(tourMap.nextStop.poiId) },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Next stop", color = Color(0xFF5E7285), style = MaterialTheme.typography.labelLarge)
                    Text(
                        tourMap.nextStop.name,
                        color = PrimaryInk,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${tourMap.nextStop.distanceLabel} - ${tourMap.nextStop.walkTimeLabel}",
                        color = Color(0xFF5E7285),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    FilterChip(selected = false, onClick = onOpenAr, label = { Text("View in AR") })
                }
            }
        }

        item { SectionTitle("Tour stops") }

        items(tourMap.stops) { stop ->
            TourStopCard(stop = stop, onClick = { onStopClick(stop.poiId) })
        }
    }
}
