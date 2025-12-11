package com.autoparts.presentation.Login

interface LoginUiEvent {
    data class emailChanged(val email: String) : LoginUiEvent
    data class passwordChanged(val password: String) : LoginUiEvent
    data class phoneNumberChanged(val phoneNumber: String) : LoginUiEvent
    data object registerModeClicked: LoginUiEvent
    data object submitRegistration: LoginUiEvent
    data object submitLogin: LoginUiEvent
    data object loginModeClicked: LoginUiEvent
    data object userMessageShown: LoginUiEvent
}