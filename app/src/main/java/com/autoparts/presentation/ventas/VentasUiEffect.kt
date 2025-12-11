package com.autoparts.presentation.ventas

sealed interface VentasUiEffect {
    data class NavigateToVentaDetalle(val ventaId: Int) : VentasUiEffect
    data object NavigateToLogin : VentasUiEffect
    object NavigateBack : VentasUiEffect
}