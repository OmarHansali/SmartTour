package com.smarttour.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttour.domain.GetPOIsUseCase
import com.smarttour.domain.POI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class POIViewModel(
    private val getPOIsUseCase: GetPOIsUseCase
) : ViewModel() {

    private val _pois = MutableStateFlow<List<POI>>(emptyList())
    val pois: StateFlow<List<POI>> = _pois

    fun loadPOIs() {
        viewModelScope.launch {
            _pois.value = getPOIsUseCase()
        }
    }
}