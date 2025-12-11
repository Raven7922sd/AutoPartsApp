package com.autoparts.presentation.miscitas

data class MisCitasState(
    val citas: List<CitaUI> = emptyList(),
    val citasFiltradas: List<CitaUI> = emptyList(),
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val filtroActual: Boolean? = null
)