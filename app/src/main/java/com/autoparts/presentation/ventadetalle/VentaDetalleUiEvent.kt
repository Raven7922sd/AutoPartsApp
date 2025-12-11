package com.autoparts.presentation.ventadetalle

sealed interface VentaDetalleUiEvent {
    data class LoadVenta(val ventaId: Int) : VentaDetalleUiEvent
    data object ClearError : VentaDetalleUiEvent
}