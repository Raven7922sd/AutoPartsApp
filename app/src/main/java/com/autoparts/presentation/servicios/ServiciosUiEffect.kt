package com.autoparts.presentation.servicios

sealed interface ServiciosUiEffect {
    data class NavigateToServicioDetalle(val servicioId: Int) : ServiciosUiEffect
    data class NavigateToCrearCita(val servicioId: Int) : ServiciosUiEffect
}