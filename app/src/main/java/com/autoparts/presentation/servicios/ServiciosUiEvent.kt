package com.autoparts.presentation.servicios

sealed interface ServiciosUiEvent {
    data object LoadServicios : ServiciosUiEvent
    data class OnSearchQueryChanged(val query: String) : ServiciosUiEvent
    data class OnServicioClick(val servicioId: Int) : ServiciosUiEvent
}