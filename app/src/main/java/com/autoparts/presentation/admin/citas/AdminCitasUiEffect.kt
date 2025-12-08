package com.autoparts.presentation.admin.citas

sealed interface AdminCitasUiEffect {
    data object NavigateBack : AdminCitasUiEffect
    data object NavigateToLogin : AdminCitasUiEffect
}