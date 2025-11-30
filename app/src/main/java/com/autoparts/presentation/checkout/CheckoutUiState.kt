package com.autoparts.presentation.checkout

import com.autoparts.dominio.model.Venta

data class CheckoutUiState(
    val nombreTitular: String = "",
    val numeroTarjeta: String = "",
    val fechaExpiracion: String = "",
    val cvv: String = "",
    val direccion: String = "",
    val nombreTitularError: String? = null,
    val numeroTarjetaError: String? = null,
    val fechaExpiracionError: String? = null,
    val cvvError: String? = null,
    val direccionError: String? = null,
    val isProcessing: Boolean = false,
    val venta: Venta? = null,
    val userMessage: String? = null,
    val checkoutSuccess: Boolean = false
)