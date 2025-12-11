package com.autoparts.data.remote.dto.venta

import com.google.gson.annotations.SerializedName

data class VentaDto(
    @SerializedName("ventaId")
    val ventaId: Int,
    @SerializedName("applicationUserId")
    val applicationUserId: String,
    @SerializedName("fecha")
    val fecha: String,
    @SerializedName("total")
    val total: Double,
    @SerializedName("detalles")
    val detalles: List<VentaDetalleDto>,
    @SerializedName("pago")
    val pago: PagoInfoDto
)

data class VentaDetalleDto(
    @SerializedName("detalleId")
    val detalleId: Int,
    @SerializedName("productoId")
    val productoId: Int,
    @SerializedName("productoNombre")
    val productoNombre: String,
    @SerializedName("cantidad")
    val cantidad: Double,
    @SerializedName("precioUnitario")
    val precioUnitario: Double,
    @SerializedName("subtotal")
    val subtotal: Double
)

data class PagoInfoDto(
    @SerializedName("pagoId")
    val pagoId: Int,
    @SerializedName("nombreTitular")
    val nombreTitular: String,
    @SerializedName("numeroTarjetaEnmascarado")
    val numeroTarjetaEnmascarado: String,
    @SerializedName("direccion")
    val direccion: String
)