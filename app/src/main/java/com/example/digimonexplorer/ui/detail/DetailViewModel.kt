package com.example.digimonexplorer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digimonexplorer.data.model.DigimonDetailResponse
import com.example.digimonexplorer.data.repository.DigimonRepository
import com.example.digimonexplorer.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: DigimonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DigimonDetailResponse>>(UiState.Loading)
    val uiState: StateFlow<UiState<DigimonDetailResponse>> = _uiState.asStateFlow()

    private var currentId: Int? = null

    fun fetchDigimonDetail(id: Int) {
        currentId = id
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val detail = repository.getDigimonDetail(id)
                _uiState.value = UiState.Success(detail)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.localizedMessage ?: "Terjadi kesalahan saat memuat detail Digimon"
                )
            }
        }
    }

    fun retry() {
        currentId?.let { fetchDigimonDetail(it) }
    }
}
