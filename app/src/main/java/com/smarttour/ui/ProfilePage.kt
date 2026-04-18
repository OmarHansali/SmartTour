package com.smarttour.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smarttour.domain.Profile

@Composable
internal fun ProfilePage(profile: Profile) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF4F7FB))
            .padding(16.dp)
    ) {
        Text(
            text = "My profile",
            style = MaterialTheme.typography.headlineMedium,
            color = PrimaryInk,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(AccentPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.initials,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = profile.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryInk,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = profile.levelLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5E7285)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            profile.stats.forEach { stat ->
                ProfileStatCard(stat = stat, modifier = Modifier.fillMaxWidth())
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        profile.settings.forEach { setting ->
            ProfileSettingRow(setting)
        }
    }
}
