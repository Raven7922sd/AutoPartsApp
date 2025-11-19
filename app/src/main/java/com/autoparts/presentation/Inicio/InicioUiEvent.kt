package com.autoparts.presentation.Inicio

interface InicioUiEvent {
    data class LoadUser(val usuarioId: Int) : InicioUiEvent
    data class UserNameChanged(val userName: String) : InicioUiEvent
    data class PasswordChanged(val password: String) : InicioUiEvent
    data object Save : InicioUiEvent
    data object Logout : InicioUiEvent
    data object UserMessageShown : InicioUiEvent
    data object showDialogEdit: InicioUiEvent
    data object hideDialogEdit: InicioUiEvent
}

