package com.autoparts.presentation.ventadetalle

sealed interface VentaDetalleUiEffect {
    object NavigateBack : VentaDetalleUiEffect
}