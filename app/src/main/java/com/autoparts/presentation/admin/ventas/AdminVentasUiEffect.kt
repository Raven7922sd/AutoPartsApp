package com.autoparts.presentation.admin.ventas

sealed interface AdminVentasUiEffect {
    data object NavigateBack : AdminVentasUiEffect
    data object NavigateToLogin : AdminVentasUiEffect
}