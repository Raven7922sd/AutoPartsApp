package com.autoparts.presentation.crearcita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateCita
import com.autoparts.domain.usecase.citas.CreateCitaUseCase
import com.autoparts.domain.usecase.servicios.GetServicioUseCase
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
class CrearCitaViewModel @Inject constructor(
    private val getServicioUseCase: GetServicioUseCase,
    private val createCitaUseCase: CreateCitaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearCitaUiState())
    val uiState: StateFlow<CrearCitaUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CrearCitaUiEffect>()
    val uiEffect: SharedFlow<CrearCitaUiEffect> = _uiEffect.asSharedFlow()

    fun onEvent(event: CrearCitaUiEvent) {
        when (event) {
            is CrearCitaUiEvent.LoadServicio -> loadServicio(event.servicioId)
            is CrearCitaUiEvent.OnNombreChanged -> onNombreChanged(event.nombre)
            is CrearCitaUiEvent.OnFechaChanged -> onFechaChanged(event.fecha)
            is CrearCitaUiEvent.OnHoraChanged -> onHoraChanged(event.hora)
            is CrearCitaUiEvent.OnSubmitCita -> onSubmitCita()
            is CrearCitaUiEvent.OnDismissDialog -> onDismissDialog()
            is CrearCitaUiEvent.OnNavigateBack -> onNavigateBack()
        }
    }

    private fun loadServicio(servicioId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = getServicioUseCase(servicioId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        servicio = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message ?: "Error al cargar servicio",
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun onNombreChanged(nombre: String) {
        _uiState.value = _uiState.value.copy(
            clienteNombre = nombre,
            nombreError = null
        )
    }

    private fun onFechaChanged(fecha: String) {
        _uiState.value = _uiState.value.copy(
            fechaCita = fecha,
            fechaError = null
        )
    }

    private fun onHoraChanged(hora: String) {
        _uiState.value = _uiState.value.copy(
            horaCita = hora,
            horaError = null
        )
    }

    private fun onSubmitCita() {
        viewModelScope.launch {
            val state = _uiState.value
            var hasError = false

            if (state.clienteNombre.isBlank()) {
                _uiState.value = _uiState.value.copy(nombreError = "El nombre es requerido")
                hasError = true
            }

            if (state.fechaCita.isBlank()) {
                _uiState.value = _uiState.value.copy(fechaError = "La fecha es requerida")
                hasError = true
            }

            if (state.horaCita.isBlank()) {
                _uiState.value = _uiState.value.copy(horaError = "La hora es requerida")
                hasError = true
            }

            if (hasError) return@launch

            _uiState.value = _uiState.value.copy(isLoading = true)

            val createCita = CreateCita(
                clienteNombre = state.clienteNombre,
                servicioSolicitado = state.servicio?.nombre ?: "",
                fechaCita = "${state.fechaCita}T${state.horaCita}:00"
            )

            when (val result = createCitaUseCase(createCita)) {
                is Resource.Success -> {
                    val cita = result.data
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        citaCreada = true,
                        codigoConfirmacion = cita?.codigoConfirmacion
                    )
                    _uiEffect.emit(CrearCitaUiEffect.ShowMessage("Cita agendada exitosamente"))
                    _uiEffect.emit(CrearCitaUiEffect.NavigateToMisCitas(cita?.citaId ?: 0))
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message ?: "Error al crear cita",
                        isLoading = false
                    )
                    _uiEffect.emit(CrearCitaUiEffect.ShowMessage(result.message ?: "Error al crear cita"))
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun onDismissDialog() {
        _uiState.value = _uiState.value.copy(
            citaCreada = false,
            codigoConfirmacion = null
        )
        viewModelScope.launch {
            _uiEffect.emit(CrearCitaUiEffect.NavigateBack)
        }
    }

    private fun onNavigateBack() {
        viewModelScope.launch {
            _uiEffect.emit(CrearCitaUiEffect.NavigateBack)
        }
    }
}