package com.autoparts.presentation.servicios

data class ServiciosUiState(
    val isLoading: Boolean = false,
    val servicios: List<ServicioUI> = emptyList(),
    val serviciosFiltrados: List<ServicioUI> = emptyList(),
    val servicioSeleccionado: ServicioUI? = null,
    val error: String? = null,
    val searchQuery: String = ""
)