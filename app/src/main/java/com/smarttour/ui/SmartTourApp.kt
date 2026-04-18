package com.smarttour.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private val AppBackground = Color(0xFFF4F7FB)
internal val ExploreBackground = Color(0xFFE6F1FB)
internal val PrimaryInk = Color(0xFF0B2D4A)
internal val AccentBlue = Color(0xFF378ADD)
internal val AccentGreen = Color(0xFF1D9E75)
internal val AccentPurple = Color(0xFF534AB7)
internal val AccentAmber = Color(0xFFBA7517)
internal val AccentCoral = Color(0xFFD85A30)
internal val SoftBorder = Color(0xFFD4DFEA)

@Composable
fun SmartTourApp(viewModel: SmartTourViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMvp()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        when {
            uiState.isLoading -> FullScreenLoading()
            uiState.errorMessage != null && uiState.exploreFeed == null -> FullScreenError(uiState.errorMessage!!)
            else -> SmartTourScaffold(uiState = uiState, viewModel = viewModel)
        }
    }
}

@Composable
private fun SmartTourScaffold(uiState: SmartTourUiState, viewModel: SmartTourViewModel) {
    val showBottomBar = uiState.currentDestination != SmartTourDestination.DETAIL

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            if (showBottomBar) {
                SmartTourBottomBar(
                    currentDestination = uiState.currentDestination,
                    onNavigate = { destination ->
                        when (destination) {
                            SmartTourDestination.EXPLORE -> viewModel.navigateTo(SmartTourDestination.EXPLORE)
                            SmartTourDestination.AR -> viewModel.openAr(uiState.arCamera?.focusedPoiId)
                            SmartTourDestination.MAP -> viewModel.openMap()
                            SmartTourDestination.PROFILE -> viewModel.openProfile()
                            SmartTourDestination.DETAIL -> Unit
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState.currentDestination) {
                SmartTourDestination.EXPLORE -> uiState.exploreFeed?.let {
                    ExplorePage(
                        feed = it,
                        onPoiClick = viewModel::openPoiDetail,
                        onTourClick = { viewModel.openMap() },
                        onOpenAr = { viewModel.openAr() }
                    )
                }

                SmartTourDestination.AR -> uiState.arCamera?.let {
                    ArCameraPage(arCamera = it, onOverlayClick = viewModel::openPoiDetail)
                }

                SmartTourDestination.MAP -> uiState.activeTour?.let {
                    TourMapPage(
                        tourMap = it,
                        onStopClick = viewModel::openPoiDetail,
                        onOpenAr = { viewModel.openAr(it.nextStop.poiId) }
                    )
                }

                SmartTourDestination.PROFILE -> uiState.profile?.let {
                    ProfilePage(profile = it)
                }

                SmartTourDestination.DETAIL -> {
                    if (uiState.isDetailLoading) {
                        FullScreenLoading()
                    } else {
                        uiState.selectedPoiDetail?.let {
                            PoiDetailPage(
                                poiDetail = it,
                                onBack = viewModel::backFromDetail,
                                onNavigate = viewModel::openMap,
                                onPlayNarration = { viewModel.openAr(it.id) },
                                onAddToTour = viewModel::openMap
                            )
                        }
                    }
                }
            }

            if (uiState.errorMessage != null && uiState.exploreFeed != null) {
                Text(
                    text = uiState.errorMessage,
                    color = Color(0xFF7D2121),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .background(Color(0xFFFFE5E5))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SmartTourBottomBar(
    currentDestination: SmartTourDestination,
    onNavigate: (SmartTourDestination) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        listOf(
            SmartTourDestination.EXPLORE to "Explore",
            SmartTourDestination.AR to "AR",
            SmartTourDestination.MAP to "Map",
            SmartTourDestination.PROFILE to "Profile"
        ).forEach { (destination, label) ->
            NavigationBarItem(
                selected = currentDestination == destination,
                onClick = { onNavigate(destination) },
                icon = {
                    Box(
                        modifier = Modifier
                            .background(if (currentDestination == destination) AccentPurple else Color(0xFFB8C0CC))
                            .padding(5.dp)
                    )
                },
                label = { Text(label) }
            )
        }
    }
}

@Composable
internal fun FullScreenLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AccentPurple)
    }
}

@Composable
internal fun FullScreenError(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color(0xFF7D2121))
    }
}
