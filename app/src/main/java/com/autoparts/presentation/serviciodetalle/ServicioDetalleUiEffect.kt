package com.autoparts.presentation.serviciodetalle

sealed interface ServicioDetalleUiEffect {
    data class NavigateToAgendarCita(val servicioId: Int) : ServicioDetalleUiEffect
    data object NavigateToLogin : ServicioDetalleUiEffect
    data object NavigateBack : ServicioDetalleUiEffect
    data class ShowMessage(val message: String) : ServicioDetalleUiEffect
}