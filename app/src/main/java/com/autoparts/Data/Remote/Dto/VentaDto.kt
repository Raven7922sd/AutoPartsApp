package com.autoparts.Data.Remote.Dto

data class VentaResponseDto(
    val ventaId: Int,
    val applicationUserId: String,
    val fecha: String,
    val total: Double,
    val detalles: List<VentaDetalleResponseDto>,
    val pago: PagoInfoDto
)

data class VentaDetalleResponseDto(
    val detalleId: Int,
    val productoId: Int,
    val productoNombre: String,
    val cantidad: Double,
    val precioUnitario: Double,
    val subtotal: Double
)

data class PagoInfoDto(
    val pagoId: Int,
    val nombreTitular: String,
    val numeroTarjetaEnmascarado: String,
    val direccion: String
)

data class CreateVentaDto(
    val pago: PagoDto
)

data class PagoDto(
    val nombreTitular: String,
    val numeroTarjeta: String,
    val fechaExpiracion: String,
    val cvv: String,
    val direccion: String
)