package com.autoparts.presentation.checkout

sealed interface CheckoutUiEvent {
    data class NombreTitularChanged(val nombre: String) : CheckoutUiEvent
    data class NumeroTarjetaChanged(val numero: String) : CheckoutUiEvent
    data class FechaExpiracionChanged(val fecha: String) : CheckoutUiEvent
    data class CvvChanged(val cvv: String) : CheckoutUiEvent
    data class DireccionChanged(val direccion: String) : CheckoutUiEvent
    data object ProcessCheckout : CheckoutUiEvent
    data object UserMessageShown : CheckoutUiEvent
    data object DismissSuccess : CheckoutUiEvent
}