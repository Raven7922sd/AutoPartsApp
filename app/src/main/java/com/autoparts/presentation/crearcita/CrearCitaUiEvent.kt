package com.autoparts.presentation.crearcita

sealed interface CrearCitaUiEvent {
    data class LoadServicio(val servicioId: Int) : CrearCitaUiEvent
    data class OnNombreChanged(val nombre: String) : CrearCitaUiEvent
    data class OnFechaChanged(val fecha: String) : CrearCitaUiEvent
    data class OnHoraChanged(val hora: String) : CrearCitaUiEvent
    data object OnSubmitCita : CrearCitaUiEvent
    data object OnDismissDialog : CrearCitaUiEvent
    data object OnNavigateBack : CrearCitaUiEvent
}