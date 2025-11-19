package com.autoparts.presentation.Inicio

sealed interface Efecto {
    data object NavigateLogin : Efecto
}