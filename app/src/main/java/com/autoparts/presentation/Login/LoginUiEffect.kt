package com.autoparts.presentation.login

sealed interface LoginUiEffect {
    data class NavigateToHome(val userId: String) : LoginUiEffect
    data class ShowMessage(val message: String) : LoginUiEffect
    object NavigateBack : LoginUiEffect
}