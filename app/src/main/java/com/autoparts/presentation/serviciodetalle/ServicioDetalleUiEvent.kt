package com.autoparts.presentation.serviciodetalle

sealed interface ServicioDetalleUiEvent {
    data class LoadServicio(val servicioId: Int) : ServicioDetalleUiEvent
    data object OnAgendarCita : ServicioDetalleUiEvent
    data object OnBack : ServicioDetalleUiEvent
}