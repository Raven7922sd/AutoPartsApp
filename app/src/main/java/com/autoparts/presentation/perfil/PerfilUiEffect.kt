package com.autoparts.presentation.perfil

sealed interface PerfilUiEffect {
    data object NavigateToVentas : PerfilUiEffect
    data object NavigateToMisCitas : PerfilUiEffect
    data object NavigateToLogin : PerfilUiEffect
    data class ShowMessage(val message: String) : PerfilUiEffect
}