package com.autoparts.presentation.miscitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.data.local.manager.SessionManager
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.usecase.citas.CancelCitaUseCase
import com.autoparts.domain.usecase.citas.GetCitasUseCase
import com.autoparts.presentation.mapper.CitaUIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MisCitasViewModel @Inject constructor(
    private val getCitasUseCase: GetCitasUseCase,
    private val cancelCitaUseCase: CancelCitaUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(MisCitasState())
    val state: StateFlow<MisCitasState> = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MisCitasUiEffect>()
    val uiEffect: SharedFlow<MisCitasUiEffect> = _uiEffect.asSharedFlow()

    init {
        checkLoginAndLoadCitas()
    }

    private fun checkLoginAndLoadCitas() {
        viewModelScope.launch {
            val isLoggedIn = sessionManager.isLoggedIn().first()
            if (!isLoggedIn) {
                _uiEffect.emit(MisCitasUiEffect.NavigateToLogin)
            } else {
                handleIntent(MisCitasIntent.CargarCitas)
            }
        }
    }

    fun handleIntent(intent: MisCitasIntent) {
        when (intent) {
            is MisCitasIntent.CargarCitas -> cargarCitas()
            is MisCitasIntent.CancelarCita -> cancelarCita(intent.citaId)
            is MisCitasIntent.FiltrarPorEstado -> filtrarPorEstado(intent.soloConfirmadas)
        }
    }

    private fun cargarCitas() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = getCitasUseCase()) {
                is Resource.Success -> {
                    val citasUI = CitaUIMapper.toUIList(result.data ?: emptyList())
                    _state.value = _state.value.copy(
                        citas = citasUI,
                        citasFiltradas = citasUI,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun cancelarCita(citaId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isProcessing = true, error = null)
            when (val result = cancelCitaUseCase(citaId)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isProcessing = false,
                        successMessage = "Cita cancelada exitosamente"
                    )
                    cargarCitas()
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message ?: "Error al cancelar cita",
                        isProcessing = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isProcessing = true)
                }
            }
        }
    }

    private fun filtrarPorEstado(soloConfirmadas: Boolean?) {
        val filtradas = if (soloConfirmadas == null) {
            _state.value.citas
        } else {
            _state.value.citas.filter { it.confirmada == soloConfirmadas }
        }
        _state.value = _state.value.copy(
            citasFiltradas = filtradas,
            filtroActual = soloConfirmadas
        )
    }
}