package com.smarttour.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttour.domain.GetActiveTourMapUseCase
import com.smarttour.domain.MarkStopVisitedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val getActiveTourMapUseCase: GetActiveTourMapUseCase,
    private val markStopVisitedUseCase: MarkStopVisitedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun loadRoute(tourId: String) {
        viewModelScope.launch {
            _uiState.value = MapUiState.Loading
            runCatching { getActiveTourMapUseCase(tourId) }
                .onSuccess { route -> _uiState.value = MapUiState.Success(route) }
                .onFailure { e ->
                    _uiState.value = MapUiState.Error(
                        e.message ?: "Failed to load tour route"
                    )
                }
        }
    }

    fun markCurrentStopVisited(tourId: String, stopId: String) {
        viewModelScope.launch {
            // Optimistically update the local UI state first
            val currentState = _uiState.value
            if (currentState is MapUiState.Success) {
                val updatedStops = currentState.route.stops.map { stop ->
                    if (stop.id == stopId) stop.copy(isVisited = true) else stop
                }
                val nextIndex = (currentState.route.currentStopIndex + 1)
                    .coerceAtMost(updatedStops.lastIndex)
                _uiState.value = MapUiState.Success(
                    currentState.route.copy(
                        stops = updatedStops,
                        currentStopIndex = nextIndex
                    )
                )
            }

            // Persist to backend; revert optimistic update on failure
            runCatching { markStopVisitedUseCase(tourId, stopId) }
                .onFailure { e ->
                    // Restore previous state so the UI reflects the actual server state
                    _uiState.value = currentState
                    _uiState.value = MapUiState.Error(
                        e.message ?: "Could not mark stop as visited"
                    )
                }
        }
    }
}
