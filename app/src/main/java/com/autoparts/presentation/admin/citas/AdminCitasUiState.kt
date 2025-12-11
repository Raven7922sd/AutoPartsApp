package com.autoparts.presentation.admin.citas

import com.autoparts.domain.model.Cita

data class AdminCitasUiState(
    val isLoading: Boolean = false,
    val citas: List<Cita> = emptyList(),
    val citasFiltradas: List<Cita> = emptyList(),
    val error: String? = null,
    val isProcessing: Boolean = false,
    val successMessage: String? = null,
    val filtroEstado: FiltroEstadoCita = FiltroEstadoCita.TODAS,
    val searchQuery: String = ""
)

enum class FiltroEstadoCita {
    TODAS,
    APROBADAS,
    PENDIENTES,
    DENEGADAS
}