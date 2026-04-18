package com.smarttour.ui.map

import com.smarttour.domain.TourRoute

sealed interface MapUiState {
    data object Loading : MapUiState
    data class Success(val route: TourRoute) : MapUiState
    data class Error(val message: String) : MapUiState
}
