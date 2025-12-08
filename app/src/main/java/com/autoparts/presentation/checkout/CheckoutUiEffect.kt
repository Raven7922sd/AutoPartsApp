package com.autoparts.presentation.checkout

sealed interface CheckoutUiEffect {
    data class ShowMessage(val message: String) : CheckoutUiEffect
    object NavigateBack : CheckoutUiEffect
}