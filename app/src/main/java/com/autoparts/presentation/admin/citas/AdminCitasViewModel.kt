package com.autoparts.presentation.admin.citas
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.usecase.citas.CancelCitaUseCase
import com.autoparts.domain.usecase.citas.ConfirmCitaUseCase
import com.autoparts.domain.usecase.citas.GetCitasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AdminCitasViewModel @Inject constructor(
    private val getCitasUseCase: GetCitasUseCase,
    private val confirmCitaUseCase: ConfirmCitaUseCase,
    private val cancelCitaUseCase: CancelCitaUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AdminCitasUiState())
    val state: StateFlow<AdminCitasUiState> = _state.asStateFlow()
    private val _effects = MutableSharedFlow<AdminCitasUiEffect>()
    val effects: SharedFlow<AdminCitasUiEffect> = _effects.asSharedFlow()
    init {
        loadCitas()
    }
    fun onEvent(event: AdminCitasUiEvent) {
        when (event) {
            is AdminCitasUiEvent.LoadCitas -> loadCitas()
            is AdminCitasUiEvent.ConfirmarCita -> confirmarCita(event.citaId)
            is AdminCitasUiEvent.RechazarCita -> rechazarCita(event.citaId)
            is AdminCitasUiEvent.ClearError -> _state.update { it.copy(error = null) }
            is AdminCitasUiEvent.ClearSuccess -> _state.update { it.copy(successMessage = null) }
            is AdminCitasUiEvent.OnFiltroEstadoChanged -> onFiltroEstadoChanged(event.filtro)
            is AdminCitasUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
        }
    }
    private fun loadCitas() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getCitasUseCase()) {
                is Resource.Success -> {
                    val citas = result.data ?: emptyList()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            citas = citas,
                            citasFiltradas = citas
                        )
                    }
                    aplicarFiltros()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error al cargar citas"
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
    private fun confirmarCita(citaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }

            when (val result = confirmCitaUseCase(citaId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            successMessage = "Cita confirmada exitosamente"
                        )
                    }
                    loadCitas()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = result.message ?: "Error al confirmar cita"
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isProcessing = true) }
                }
            }
        }
    }
    private fun rechazarCita(citaId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }
            when (val result = cancelCitaUseCase(citaId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            successMessage = "Cita rechazada exitosamente"
                        )
                    }
                    loadCitas()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = result.message ?: "Error al rechazar cita"
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isProcessing = true) }
                }
            }
        }
    }
    private fun onFiltroEstadoChanged(filtro: FiltroEstadoCita) {
        _state.update { it.copy(filtroEstado = filtro) }
        aplicarFiltros()
    }
    private fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }
    private fun aplicarFiltros() {
        val state = _state.value
        var citasFiltradas = state.citas
        citasFiltradas = when (state.filtroEstado) {
            FiltroEstadoCita.TODAS -> citasFiltradas
            FiltroEstadoCita.APROBADAS -> citasFiltradas.filter { it.confirmada }
            FiltroEstadoCita.PENDIENTES -> citasFiltradas.filter { !it.confirmada }
            FiltroEstadoCita.DENEGADAS -> emptyList()
        }
        if (state.searchQuery.isNotBlank()) {
            citasFiltradas = citasFiltradas.filter { cita ->
                cita.clienteNombre.contains(state.searchQuery, ignoreCase = true) ||
                cita.servicioSolicitado.contains(state.searchQuery, ignoreCase = true) ||
                cita.codigoConfirmacion.contains(state.searchQuery, ignoreCase = true)
            }
        }
        _state.update { it.copy(citasFiltradas = citasFiltradas) }
    }
}