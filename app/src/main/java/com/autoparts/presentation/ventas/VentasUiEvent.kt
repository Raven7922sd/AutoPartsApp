package com.autoparts.presentation.ventas

sealed interface VentasUiEvent {
    data object LoadVentas : VentasUiEvent
    data object ClearError : VentasUiEvent
}

