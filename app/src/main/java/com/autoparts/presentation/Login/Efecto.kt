package com.autoparts.presentation.Login


sealed interface Efecto {
    data class NavigateHome(val userId: String) : Efecto
}