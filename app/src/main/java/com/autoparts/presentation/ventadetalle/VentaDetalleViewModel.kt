package com.autoparts.presentation.ventadetalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.usecase.GetVentaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentaDetalleViewModel @Inject constructor(
    private val getVentaUseCase: GetVentaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VentaDetalleUiState())
    val state: StateFlow<VentaDetalleUiState> = _state.asStateFlow()

    fun onEvent(event: VentaDetalleUiEvent) {
        when (event) {
            is VentaDetalleUiEvent.LoadVenta -> loadVenta(event.ventaId)
            VentaDetalleUiEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun loadVenta(ventaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getVentaUseCase(ventaId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            venta = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message ?: "Error al cargar la venta",
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }
}