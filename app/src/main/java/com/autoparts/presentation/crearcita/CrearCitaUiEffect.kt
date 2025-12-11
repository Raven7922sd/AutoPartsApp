package com.autoparts.presentation.crearcita

sealed interface CrearCitaUiEffect {
    data object NavigateBack : CrearCitaUiEffect
    data class ShowMessage(val message: String) : CrearCitaUiEffect
    data class NavigateToMisCitas(val citaId: Int) : CrearCitaUiEffect
}