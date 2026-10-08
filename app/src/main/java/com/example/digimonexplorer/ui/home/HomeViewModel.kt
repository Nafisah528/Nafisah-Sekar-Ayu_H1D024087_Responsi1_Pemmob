package com.example.digimonexplorer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digimonexplorer.data.model.DigimonCardItem
import com.example.digimonexplorer.data.repository.DigimonRepository
import com.example.digimonexplorer.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Home Screen.
 * Mengelola pemanggilan data Digimon dan menyediakan StateFlow UI State.
 */
class HomeViewModel(
    private val repository: DigimonRepository
) : ViewModel() {

    // StateFlow untuk membungkus kondisi: Loading, Success, dan Error
    private val _uiState = MutableStateFlow<UiState<List<DigimonCardItem>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<DigimonCardItem>>> = _uiState.asStateFlow()

    init {
        fetchDigimonList()
    }

    /**
     * Memuat daftar Digimon dari repository dan memperbarui UI state.
     */
    fun fetchDigimonList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val list = repository.getDigimonList()
                _uiState.value = UiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.localizedMessage ?: "Terjadi kesalahan saat memuat daftar Digimon. Silakan coba lagi."
                )
            }
        }
    }
}
