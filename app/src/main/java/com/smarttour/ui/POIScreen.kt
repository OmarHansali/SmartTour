package com.smarttour.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun POIScreen(viewModel: POIViewModel) {

    val pois by viewModel.pois.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPOIs()
    }

    LazyColumn {
        items(pois) { poi ->
            Card {
                Column {
                    Text(poi.name, style = MaterialTheme.typography.titleMedium)
                    Text(poi.description)
                }
            }
        }
    }
}