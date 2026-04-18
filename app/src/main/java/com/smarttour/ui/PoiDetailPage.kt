package com.smarttour.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttour.domain.PoiDetail

@Composable
internal fun PoiDetailPage(
    poiDetail: PoiDetail,
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    onPlayNarration: () -> Unit,
    onAddToTour: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF4F7FB))
            .padding(16.dp)
    ) {
        Text(
            text = "Back",
            color = AccentPurple,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.clickable { onBack() }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(colors = listOf(Color(0xFFB8D4A4), Color(0xFFD4E7BA)))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = poiDetail.eraLabel,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = poiDetail.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = PrimaryInk,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = poiDetail.subtitle,
                    color = Color(0xFF5E7285),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = poiDetail.status,
                color = AccentGreen,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .background(Color(0xFFE1F5EE), RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DetailChip(poiDetail.ratingLabel, AccentAmber)
            DetailChip(poiDetail.distanceLabel, AccentGreen)
            DetailChip(poiDetail.admissionLabel, Color(0xFF7B8794))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = poiDetail.description,
            style = MaterialTheme.typography.bodyLarge,
            color = PrimaryInk,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = poiDetail.actions.canPlayNarration) { onPlayNarration() },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F7F1)),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = poiDetail.narrationTitle,
                    color = Color(0xFF085041),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = poiDetail.narrationSubtitle,
                    color = AccentGreen,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionButton(
                text = "Add to tour",
                background = Color.White,
                contentColor = PrimaryInk,
                modifier = Modifier.fillMaxWidth(),
                onClick = onAddToTour
            )
            ActionButton(
                text = "Navigate",
                background = Color(0xFFEAE8FA),
                contentColor = AccentPurple,
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigate
            )
        }
    }
}
