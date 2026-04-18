package com.smarttour.ui.map

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.smarttour.domain.TourRoute
import com.smarttour.domain.TourStop
import com.smarttour.ui.map.components.MapTopBar
import com.smarttour.ui.map.components.NextStopCard
import com.smarttour.ui.map.components.TourMapLegend
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    tourId: String,
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(tourId) {
        viewModel.loadRoute(tourId)
    }

    when (val state = uiState) {
        is MapUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MapUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        is MapUiState.Success -> {
            MapContent(
                route = state.route,
                onBackClick = onBackClick,
                onMarkVisited = { stopId ->
                    viewModel.markCurrentStopVisited(tourId, stopId)
                }
            )
        }
    }
}

@Composable
private fun MapContent(
    route: TourRoute,
    onBackClick: () -> Unit,
    onMarkVisited: (String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            MapTopBar(
                tourName = route.tourName,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // OSMDroid Map
            TourMapView(
                context = context,
                stops = route.stops,
                modifier = Modifier.fillMaxSize()
            )

            // Legend overlay (top-start of map)
            TourMapLegend(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )

            // Next stop card pinned to bottom
            NextStopCard(
                currentStop = route.currentStop,
                nextStop = route.nextStop,
                progressFraction = route.progressFraction,
                onMarkVisited = {
                    route.currentStop?.let { onMarkVisited(it.id) }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TourMapView(
    context: Context,
    stops: List<TourStop>,
    modifier: Modifier = Modifier
) {
    val mapView = remember {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { map ->
            map.overlays.clear()

            if (stops.isEmpty()) return@AndroidView

            // Center map on current stop or first stop
            val centerStop = stops.firstOrNull { !it.isVisited } ?: stops.first()
            map.controller.apply {
                setZoom(15.0)
                setCenter(GeoPoint(centerStop.latitude, centerStop.longitude))
            }

            // Draw route polyline
            if (stops.size > 1) {
                val polyline = Polyline().apply {
                    setPoints(stops.map { GeoPoint(it.latitude, it.longitude) })
                }
                map.overlays.add(polyline)
            }

            // Add markers for each stop
            stops.forEach { stop ->
                val marker = Marker(map).apply {
                    position = GeoPoint(stop.latitude, stop.longitude)
                    title = stop.name
                    snippet = stop.description
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                map.overlays.add(marker)
            }

            map.invalidate()
        }
    )
}
