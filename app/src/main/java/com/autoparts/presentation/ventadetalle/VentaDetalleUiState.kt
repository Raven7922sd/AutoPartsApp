package com.autoparts.presentation.ventadetalle

import com.autoparts.dominio.model.Venta

data class VentaDetalleUiState(
    val venta: Venta? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)