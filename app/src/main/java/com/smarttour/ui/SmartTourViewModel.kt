package com.smarttour.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttour.domain.ArCamera
import com.smarttour.domain.ExploreFeed
import com.smarttour.domain.GetArCameraUseCase
import com.smarttour.domain.GetExploreFeedUseCase
import com.smarttour.domain.GetPoiDetailUseCase
import com.smarttour.domain.GetProfileUseCase
import com.smarttour.domain.GetTourMapUseCase
import com.smarttour.domain.PoiDetail
import com.smarttour.domain.Profile
import com.smarttour.domain.TourMap
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class SmartTourDestination {
    EXPLORE,
    AR,
    MAP,
    PROFILE,
    DETAIL
}

data class SmartTourUiState(
    val isLoading: Boolean = true,
    val isDetailLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentDestination: SmartTourDestination = SmartTourDestination.EXPLORE,
    val exploreFeed: ExploreFeed? = null,
    val activeTour: TourMap? = null,
    val arCamera: ArCamera? = null,
    val profile: Profile? = null,
    val selectedPoiDetail: PoiDetail? = null
)

@HiltViewModel
class SmartTourViewModel @Inject constructor(
    private val getExploreFeedUseCase: GetExploreFeedUseCase,
    private val getPoiDetailUseCase: GetPoiDetailUseCase,
    private val getTourMapUseCase: GetTourMapUseCase,
    private val getArCameraUseCase: GetArCameraUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SmartTourUiState())
    val uiState: StateFlow<SmartTourUiState> = _uiState.asStateFlow()

    fun loadMvp() {
        if (_uiState.value.exploreFeed != null && !_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val explore = try { getExploreFeedUseCase() } catch (e: Exception) { null }
                val tour = try { getTourMapUseCase() } catch (e: Exception) { null }
                val ar = try { getArCameraUseCase(null) } catch (e: Exception) { null }
                val profile = try { getProfileUseCase() } catch (e: Exception) { null }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    exploreFeed = explore,
                    activeTour = tour,
                    arCamera = ar,
                    profile = profile,
                    currentDestination = SmartTourDestination.EXPLORE
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Unable to load SmartTour data."
                )
            }
        }
    }

    fun navigateTo(destination: SmartTourDestination) {
        _uiState.value = _uiState.value.copy(currentDestination = destination)
    }

    fun openPoiDetail(poiId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDetailLoading = true,
                currentDestination = SmartTourDestination.DETAIL,
                errorMessage = null
            )
            try {
                _uiState.value = _uiState.value.copy(
                    isDetailLoading = false,
                    selectedPoiDetail = getPoiDetailUseCase(poiId)
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDetailLoading = false,
                    errorMessage = exception.message ?: "Unable to load POI detail."
                )
            }
        }
    }

    fun openAr(poiId: String? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    arCamera = getArCameraUseCase(poiId),
                    currentDestination = SmartTourDestination.AR,
                    errorMessage = null
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = exception.message ?: "Unable to load AR experience."
                )
            }
        }
    }

    fun openMap() {
        _uiState.value = _uiState.value.copy(currentDestination = SmartTourDestination.MAP)
    }

    fun openProfile() {
        _uiState.value = _uiState.value.copy(currentDestination = SmartTourDestination.PROFILE)
    }

    fun backFromDetail() {
        _uiState.value = _uiState.value.copy(currentDestination = SmartTourDestination.EXPLORE)
    }
}
