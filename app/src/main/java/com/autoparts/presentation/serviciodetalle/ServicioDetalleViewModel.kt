package com.autoparts.presentation.serviciodetalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.data.local.manager.SessionManager
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.usecase.servicios.GetServicioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicioDetalleViewModel @Inject constructor(
    private val getServicioUseCase: GetServicioUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicioDetalleUiState())
    val uiState: StateFlow<ServicioDetalleUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ServicioDetalleUiEffect>()
    val uiEffect: SharedFlow<ServicioDetalleUiEffect> = _uiEffect.asSharedFlow()

    fun onEvent(event: ServicioDetalleUiEvent) {
        when (event) {
            is ServicioDetalleUiEvent.LoadServicio -> loadServicio(event.servicioId)
            is ServicioDetalleUiEvent.OnAgendarCita -> navigateToAgendarCita()
            is ServicioDetalleUiEvent.OnBack -> navigateBack()
        }
    }

    private fun loadServicio(servicioId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getServicioUseCase(servicioId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            servicio = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Error al cargar servicio",
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun navigateToAgendarCita() {
        viewModelScope.launch {
            val isLoggedIn = sessionManager.isLoggedIn().first()

            if (isLoggedIn) {
                _uiState.value.servicio?.let { servicio ->
                    _uiEffect.emit(ServicioDetalleUiEffect.NavigateToAgendarCita(servicio.servicioId))
                }
            } else {
                _uiEffect.emit(ServicioDetalleUiEffect.NavigateToLogin)
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _uiEffect.emit(ServicioDetalleUiEffect.NavigateBack)
        }
    }
}