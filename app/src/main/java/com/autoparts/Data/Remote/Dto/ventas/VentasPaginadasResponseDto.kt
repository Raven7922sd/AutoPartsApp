package com.autoparts.data.remote.dto.ventas

import com.autoparts.data.remote.dto.venta.PagoInfoDto
import com.google.gson.annotations.SerializedName

data class VentasPaginadasResponseDto(
    @SerializedName("ventas")
    val ventas: List<VentaResponseDto>,

    @SerializedName("paginaActual")
    val paginaActual: Int,

    @SerializedName("totalPaginas")
    val totalPaginas: Int,

    @SerializedName("totalVentas")
    val totalVentas: Int,

    @SerializedName("totalIngresos")
    val totalIngresos: Double
)

data class VentaResponseDto(
    @SerializedName("ventaId")
    val ventaId: Int,

    @SerializedName("applicationUserId")
    val applicationUserId: String,

    @SerializedName("nombreUsuario")
    val nombreUsuario: String? = null,

    @SerializedName("emailUsuario")
    val emailUsuario: String? = null,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("total")
    val total: Double,

    @SerializedName("detalles")
    val detalles: List<VentaDetalleResponseDto>,

    @SerializedName("pago")
    val pago: PagoInfoDto
)

data class VentaDetalleResponseDto(
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