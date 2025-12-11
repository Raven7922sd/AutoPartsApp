package com.autoparts.dominio.model

data class Venta(
    val ventaId: Int,
    val applicationUserId: String,
    val fecha: String,
    val total: Double,
    val detalles: List<VentaDetalle>,
    val pago: PagoInfo
)

data class VentaDetalle(
    val detalleId: Int,
    val productoId: Int,
    val productoNombre: String,
    val cantidad: Double,
    val precioUnitario: Double,
    val subtotal: Double
)

data class PagoInfo(
    val pagoId: Int,
    val nombreTitular: String,
    val numeroTarjetaEnmascarado: String,
    val direccion: String
)

data class CreateVenta(
    val nombreTitular: String,
    val numeroTarjeta: String,
    val fechaExpiracion: String,
    val cvv: String,
    val direccion: String
)

