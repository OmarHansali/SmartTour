package com.smarttour.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smarttour.domain.ExplorePoi
import com.smarttour.domain.FeaturedTour
import com.smarttour.domain.ProfileSetting
import com.smarttour.domain.ProfileStat
import com.smarttour.domain.TourStop

@Composable
internal fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = PrimaryInk,
        fontWeight = FontWeight.Bold
    )
}

@Composable
internal fun RoundedSearchBar(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(AccentBlue)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = AccentBlue, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
internal fun NearbyHighlightCard(poi: ExplorePoi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(categoryColor(poi.category).copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(categoryColor(poi.category))
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.fillMaxWidth(0.72f)) {
                Text(
                    text = poi.name,
                    color = PrimaryInk,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${poi.category} - ${poi.distanceMeters} m",
                    color = AccentBlue,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = poi.description,
                    color = Color(0xFF47637A),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${poi.rating} *",
                color = AccentBlue,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE9F2FB))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
internal fun FeaturedTourCard(tour: FeaturedTour, onClick: () -> Unit) {
    val accent = parseColor(tour.accentColorHex, AccentGreen)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accent.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(accent)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = tour.title,
                color = PrimaryInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = tour.description, color = Color(0xFF47637A), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${tour.durationMinutes} min - ${tour.stopCount} stops",
                color = accent,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
internal fun TourStopCard(stop: TourStop, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (stop.isCurrent) AccentPurple else AccentGreen)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.fillMaxWidth(0.72f)) {
                Text(stop.name, color = PrimaryInk, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stop.distanceLabel} - ${stop.walkTimeLabel}",
                    color = Color(0xFF5E7285),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (stop.isCurrent) {
                Text(text = "Current", color = AccentPurple, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
internal fun MetricCard(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(text = label, color = Color.White.copy(alpha = 0.55f), style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
internal fun DetailChip(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
internal fun ActionButton(
    text: String,
    background: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = contentColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun ProfileStatCard(stat: ProfileStat, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stat.value,
                style = MaterialTheme.typography.headlineSmall,
                color = PrimaryInk,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stat.label, color = Color(0xFF5E7285), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
internal fun ProfileSettingRow(setting: ProfileSetting) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = setting.label, color = PrimaryInk, style = MaterialTheme.typography.bodyLarge)
        Text(text = setting.value, color = Color(0xFF5E7285), style = MaterialTheme.typography.bodyMedium)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(SoftBorder)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreQuickActions(onOpenAr: () -> Unit, onOpenMap: () -> Unit, city: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = false, onClick = onOpenAr, label = { Text("Open AR") })
        FilterChip(selected = false, onClick = onOpenMap, label = { Text(city) })
    }
}

internal fun categoryColor(category: String): Color {
    return when (category.lowercase()) {
        "mosque" -> AccentGreen
        "palace" -> AccentPurple
        "square" -> AccentAmber
        else -> AccentBlue
    }
}

internal fun parseColor(value: String, fallback: Color): Color {
    return try {
        Color(android.graphics.Color.parseColor(value))
    } catch (_: IllegalArgumentException) {
        fallback
    }
}
