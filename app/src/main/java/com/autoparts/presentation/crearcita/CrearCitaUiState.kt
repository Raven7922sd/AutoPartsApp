package com.autoparts.presentation.crearcita

import com.autoparts.domain.model.Servicio

data class CrearCitaUiState(
    val isLoading: Boolean = false,
    val servicio: Servicio? = null,
    val clienteNombre: String = "",
    val fechaCita: String = "",
    val horaCita: String = "",
    val error: String? = null,
    val userMessage: String? = null,
    val citaCreada: Boolean = false,
    val codigoConfirmacion: String? = null,
    val nombreError: String? = null,
    val fechaError: String? = null,
    val horaError: String? = null
)