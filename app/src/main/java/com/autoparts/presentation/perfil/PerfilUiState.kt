package com.autoparts.presentation.perfil

import com.autoparts.dominio.model.Usuarios

data class PerfilUiState(
    val isLoading: Boolean = false,
    val usuario: Usuarios? = null,
    val email: String = "",
    val userName: String = "",
    val phoneNumber: String = "",
    val emailError: String? = null,
    val userNameError: String? = null,
    val phoneNumberError: String? = null,
    val showEditDialog: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)