package com.autoparts.presentation.Login

data class LoginUiState (
    val isLoading: Boolean = false,
    val userId: String = "",
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val isRegistering: Boolean = false,
    val userMessage: String = ""
)