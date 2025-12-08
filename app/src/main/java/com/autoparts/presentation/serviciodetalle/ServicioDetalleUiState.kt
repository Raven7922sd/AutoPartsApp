package com.autoparts.presentation.serviciodetalle

import com.autoparts.domain.model.Servicio

data class ServicioDetalleUiState(
    val servicio: Servicio? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)