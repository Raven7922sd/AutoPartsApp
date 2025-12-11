package com.autoparts.presentation.admin.citas

sealed interface AdminCitasUiEvent {
    data object LoadCitas : AdminCitasUiEvent
    data class ConfirmarCita(val citaId: Int) : AdminCitasUiEvent
    data class RechazarCita(val citaId: Int) : AdminCitasUiEvent
    data object ClearError : AdminCitasUiEvent
    data object ClearSuccess : AdminCitasUiEvent
    data class OnFiltroEstadoChanged(val filtro: FiltroEstadoCita) : AdminCitasUiEvent
    data class OnSearchQueryChanged(val query: String) : AdminCitasUiEvent
}