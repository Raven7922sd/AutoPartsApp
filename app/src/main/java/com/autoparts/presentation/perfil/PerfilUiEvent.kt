package com.autoparts.presentation.perfil

sealed interface PerfilUiEvent {
    data class LoadProfile(val userId: String) : PerfilUiEvent
    data class UserNameChanged(val userName: String) : PerfilUiEvent
    data class PhoneNumberChanged(val phoneNumber: String) : PerfilUiEvent
    data object ShowEditDialog : PerfilUiEvent
    data object HideEditDialog : PerfilUiEvent
    data object SaveProfile : PerfilUiEvent
    data object ClearMessages : PerfilUiEvent
    data object Logout : PerfilUiEvent
}