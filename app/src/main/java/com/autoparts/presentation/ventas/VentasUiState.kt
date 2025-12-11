package com.autoparts.presentation.ventas

import com.autoparts.dominio.model.Venta

data class VentasUiState(
    val ventas: List<Venta> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)