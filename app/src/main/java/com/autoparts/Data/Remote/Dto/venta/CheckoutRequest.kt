package com.autoparts.data.remote.dto.venta

import com.google.gson.annotations.SerializedName

data class CheckoutRequest(
    @SerializedName("pago")
    val pago: PagoRequest
)

data class PagoRequest(
    @SerializedName("nombreTitular")
    val nombreTitular: String,
    @SerializedName("numeroTarjeta")
    val numeroTarjeta: String,
    @SerializedName("fechaExpiracion")
    val fechaExpiracion: String,
    @SerializedName("cvv")
    val cvv: String,
    @SerializedName("direccion")
    val direccion: String
)