package com.autoparts.presentation.miscitas

sealed interface MisCitasUiEffect {
    data class NavigateToDetalle(val citaId: Int) : MisCitasUiEffect
    data object NavigateToLogin : MisCitasUiEffect
    data object CitaCancelada : MisCitasUiEffect
    data class ShowError(val message: String) : MisCitasUiEffect
}