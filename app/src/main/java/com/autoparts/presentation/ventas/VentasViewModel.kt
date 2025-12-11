package com.autoparts.presentation.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.dominio.usecase.GetVentasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentasViewModel @Inject constructor(
    private val getVentasUseCase: GetVentasUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VentasUiState())
    val state: StateFlow<VentasUiState> = _state.asStateFlow()

    init {
        loadVentas()
    }

    fun onEvent(event: VentasUiEvent) {
        when (event) {
            VentasUiEvent.LoadVentas -> loadVentas()
            VentasUiEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun loadVentas() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val ventas = getVentasUseCase().first()
                _state.update {
                    it.copy(
                        ventas = ventas,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Error al cargar ventas",
                        isLoading = false
                    )
                }
            }
        }
    }
}