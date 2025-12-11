package com.autoparts.presentation.carrito

sealed interface CarritoUiEffect {
    data object NavigateToCheckout : CarritoUiEffect
    data object NavigateToLogin : CarritoUiEffect
    data class ShowMessage(val message: String) : CarritoUiEffect
}