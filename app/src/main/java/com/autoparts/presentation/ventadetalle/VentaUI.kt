package com.autoparts.presentation.ventadetalle

data class VentaUI(
    val ventaId: Int,
    val fecha: String,
    val fechaFormateada: String,
    val total: Double,
    val totalFormateado: String,
    val detalles: List<VentaDetalleUI>,
    val pagoInfo: PagoInfoUI
)

data class VentaDetalleUI(
    val detalleId: Int,
    val productoId: Int,
    val productoNombre: String,
    val cantidad: Double,
    val precioUnitario: Double,
    val subtotal: Double,
    val descripcionCantidad: String,
    val subtotalFormateado: String
)

data class PagoInfoUI(
    val nombreTitular: String,
    val numeroTarjetaEnmascarado: String,
    val direccion: String
)