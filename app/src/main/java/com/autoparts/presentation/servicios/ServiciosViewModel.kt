package com.autoparts.presentation.servicios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.usecase.servicios.GetServiciosUseCase
import com.autoparts.presentation.mapper.ServicioUIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiciosViewModel @Inject constructor(
    private val getServiciosUseCase: GetServiciosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiciosUiState())
    val uiState: StateFlow<ServiciosUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ServiciosUiEffect>()
    val uiEffect: SharedFlow<ServiciosUiEffect> = _uiEffect.asSharedFlow()

    init {
        onEvent(ServiciosUiEvent.LoadServicios)
    }

    fun onEvent(event: ServiciosUiEvent) {
        when (event) {
            is ServiciosUiEvent.LoadServicios -> loadServicios()
            is ServiciosUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            is ServiciosUiEvent.OnServicioClick -> onServicioClick(event.servicioId)
        }
    }

    private fun loadServicios() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = getServiciosUseCase()) {
                is Resource.Success -> {
                    val serviciosUI = ServicioUIMapper.toUIList(result.data ?: emptyList())
                    _uiState.value = _uiState.value.copy(
                        servicios = serviciosUI,
                        serviciosFiltrados = serviciosUI,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        val filtrados = if (query.isBlank()) {
            _uiState.value.servicios
        } else {
            _uiState.value.servicios.filter { servicio ->
                servicio.nombre.contains(query, ignoreCase = true) ||
                servicio.descripcion.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(
            serviciosFiltrados = filtrados,
            searchQuery = query
        )
    }

    private fun onServicioClick(servicioId: Int) {
        viewModelScope.launch {
            _uiEffect.emit(ServiciosUiEffect.NavigateToServicioDetalle(servicioId))
        }
    }
}